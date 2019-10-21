package com.example.ds1.service;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.error.WordNotFoundException;
import com.example.ds1.mapper.WordMapper;
import com.example.ds1.model.Word;
import com.example.ds1.postagging.POSTagging;
import com.example.ds1.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.ds1.utils.Utils.createPageable;
import static com.example.ds1.utils.Utils.zeroIfNull;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final POSTagging posTagging;

    private static final Long QUERY_SIZE = 1000L;

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

    @Transactional
    public void extractWords(String file) throws Exception {
        Map<String, WordEntity> words = getWordsWithFrequency(file);
        for (int i = 0; i <= words.keySet().size() / QUERY_SIZE; i++) {
            List<String> wordsList = words.keySet()
                    .stream()
                    .skip(i * QUERY_SIZE)
                    .limit(QUERY_SIZE)
                    .collect(Collectors.toList());
            List<WordEntity> existingWords = wordRepository.findAllByWordIn(wordsList);
            existingWords.forEach(wordEntity -> {
                try {
                    WordEntity w = words.get(wordEntity.getWord());
                    if (w != null) {
                        wordEntity.setFrequency(zeroIfNull(wordEntity.getFrequency()) +
                                zeroIfNull(w.getFrequency()));
                        wordEntity.getTags().addAll(w.getTags());
                    }
                    words.remove(wordEntity.getWord());
                } catch (Exception e) { e.printStackTrace();
                    System.out.println(wordEntity);}
            });
            wordRepository.saveAll(existingWords);
        }
        wordRepository.saveAll(words.values());
    }

    protected Map<String, WordEntity> getWordsWithFrequency(String file) throws IOException {
        String path = "texts/" + file;
        String text = new String(Files.readAllBytes(Paths.get(path)));
        return posTagging.tag(text);
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
        if (!isPresent && !word.getWord().equals("")) {
            wordRepository.save(WordMapper.toEntity(word));
        }
    }
}
