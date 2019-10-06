package com.example.ds1.service;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.error.WordNotFoundException;
import com.example.ds1.mapper.WordMapper;
import com.example.ds1.model.Word;
import com.example.ds1.repository.WordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.ds1.utils.Utils.createPageable;
import static com.example.ds1.utils.Utils.zeroIfNull;

@Service
public class WordService {

    private final WordRepository wordRepository;

    private static final Long QUERY_SIZE = 1000L;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getAllWords(int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return wordRepository.findAll(pageable).get()
                .map(WordMapper::toModel).collect(Collectors.toList());
    }

    public List<Word> findWordsWithPattern(String pattern, int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return wordRepository.findAllByWordLike(pattern + "%", pageable)
                .map(WordMapper::toModel).getContent();
    }

    public void extractWords(String name) throws Exception {
        File file = new File("/Users/antonluhavy/Documents/ds1/texts/" + name);
        Map<String, Long> words = getWordsWithFrequency(file);
        for (int i = 0; i < words.keySet().size() / QUERY_SIZE; i++) {
            List<String> wordsList = words.keySet()
                    .stream()
                    .skip(i * QUERY_SIZE)
                    .limit(QUERY_SIZE)
                    .collect(Collectors.toList());
            List<WordEntity> existingWords = wordRepository.findAllByWordIn(wordsList);
            existingWords.forEach(wordEntity -> {
                Long addFrequency = words.get(wordEntity.getWord());
                wordEntity.setFrequency(wordEntity.getFrequency() + addFrequency);
                words.remove(wordEntity.getWord());
            });
            wordRepository.saveAll(existingWords);
        }
        wordRepository.saveAll(words.entrySet().stream()
                .map(w -> WordEntity.builder().word(w.getKey()).frequency(w.getValue()).build())
                .collect(Collectors.toList()));
    }

    protected Map<String, Long> getWordsWithFrequency(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        Map<String, Long> words = new HashMap<>();
        while (scanner.hasNext()) {
            String word = scanner.next()
                    .toLowerCase()
                    .replaceAll("[^\\w\\'\\-]", "")
                    .replaceAll("\\d+", "");
            if (!word.equals("-")) {
                Long frequency = zeroIfNull(words.get(word));
                words.put(word, frequency + 1);
            }
        }
        return words;
    }

    public Word changeWord(String word, Word change) {
        WordEntity wordEntity = wordRepository.findByWord(word)
                .orElseThrow(() -> new WordNotFoundException(word));
        Optional<WordEntity> optional = wordRepository.findByWord(change.getWord());
        if (optional.isPresent()) {
            WordEntity wordEntity1 = optional.get();
            wordEntity1.setFrequency(
                    wordEntity1.getFrequency() + wordEntity.getFrequency());
            wordRepository.save(wordEntity1);
            wordRepository.delete(wordEntity);
            return WordMapper.toModel(wordEntity1);
        } else {
            wordEntity.setWord(change.getWord());
            WordEntity result = wordRepository.save(wordEntity);
            return WordMapper.toModel(result);
        }
    }

    @Transactional
    public Integer deleteWord(String word) {
        return wordRepository.deleteByWord(word);
    }

    public void addWord(Word word) {
        boolean isPresent = wordRepository.findByWord(word.getWord()).isPresent();
        if (!isPresent) {
            wordRepository.save(WordMapper.toEntity(word));
        }
    }
}
