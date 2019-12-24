package com.example.ds1.service;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.entity.WordTag;
import com.example.ds1.error.WordNotFoundException;
import com.example.ds1.mapper.WordMapper;
import com.example.ds1.model.Word;
import com.example.ds1.postagging.POSTagging;
import com.example.ds1.repository.TagRepository;
import com.example.ds1.repository.WordRepository;
import lombok.var;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.ds1.utils.Utils.createPageable;
import static com.example.ds1.utils.Utils.zeroIfNull;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final POSTagging posTagging;
    private final TagRepository tagRepository;

    private static final Long QUERY_SIZE = 1000L;

    private Map<String, List<WordEntity>> texts;

    public WordService(WordRepository wordRepository, POSTagging posTagging, TagRepository tagRepository) throws IOException {
        this.wordRepository = wordRepository;
        this.posTagging = posTagging;
        this.tagRepository = tagRepository;
        texts = new HashMap<>();
        createTexts();
    }

    public void createTexts() throws IOException {
        //var txs = List.of("test.txt");
        var txs = List.of("chocofactory.txt", "clockwork_orange.txt", "do_androids_dreams.txt", "rebecca.txt", "thingsfallapart.txt", "wolf_hall.txt");
        for (var p : txs) {
            String path = "texts/" + p;
            String text = new String(Files.readAllBytes(Paths.get(path)));
            var mp = posTagging.tag(text, p);
            var v = Collections.max(mp.values(), Comparator.comparingLong(WordEntity::getFrequency)).getFrequency();
            var res = mp.values().stream().peek(w -> w.setFrequency(w.getFrequency() / v)).collect(Collectors.toList());
            texts.put(p, res);
        }
    }

    @Transactional
    public List<Word> getAllWords(int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return wordRepository.findAll(pageable).get()
                .map(w -> WordMapper.toModel(w,
                        wordRepository.getTagsByWord(w.getLemma()).stream()
                                .map(WordTag::getTagName)
                                .collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }

    public List<Word> findWordsWithPattern(String pattern, int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return wordRepository.findAllByWordLike(pattern + "%", pageable)
                .map(w -> WordMapper.toModel(w,
                        wordRepository.getTagsByWord(w.getLemma()).stream()
                                .map(WordTag::getTagName)
                                .collect(Collectors.toSet())))
                .getContent();
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
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(wordEntity);
                }
            });
            wordRepository.saveAll(existingWords);
        }
        wordRepository.saveAll(words.values());
    }

    protected Map<String, WordEntity> getWordsWithFrequency(String file) throws IOException {
        String path = "texts/" + file;
        String text = new String(Files.readAllBytes(Paths.get(path)));
        return posTagging.tag(text, file);
    }

    @Transactional
    public Word changeWord(String word, Word change) {
        WordEntity wordEntity = wordRepository.findByWord(word)
                .orElseThrow(() -> new WordNotFoundException(word));
        Optional<WordEntity> optional = wordRepository.findByWord(change.getWord());
        if (optional.isPresent()) {
            WordEntity wordEntity1 = optional.get();
            wordEntity1.setFrequency(
                    wordEntity1.getFrequency() + wordEntity.getFrequency());
            wordEntity1.setTags(change.getTags().stream().map(WordTag::new).collect(Collectors.toSet()));
            wordRepository.save(wordEntity1);
            wordRepository.delete(wordEntity);
            return WordMapper.toModel(wordEntity1,
                    wordRepository.getTagsByWord(wordEntity1.getLemma()).stream()
                            .map(WordTag::getTagName)
                            .collect(Collectors.toSet()));
        } else {
            wordEntity.setWord(change.getWord());
            wordEntity.setTags(change.getTags().stream().map(WordTag::new).collect(Collectors.toSet()));
            WordEntity result = wordRepository.save(wordEntity);
            return WordMapper.toModel(result,
                    wordRepository.getTagsByWord(result.getLemma()).stream()
                            .map(WordTag::getTagName)
                            .collect(Collectors.toSet()));
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

    public String text(String model) throws Exception {
        String path = "texts/" + model;
        String text = new String(Files.readAllBytes(Paths.get(path)));
        return posTagging.text(text);
    }

    public List<Word> stat(Word model) throws Exception {
        String path = "texts/" + model.getWord();
        String text = new String(Files.readAllBytes(Paths.get(path)));
        return posTagging.statistics(text, model.getLemma());
    }

    public List<Word> statTag(Word model) throws Exception {
        String path = "texts/" + model.getWord();
        String text = new String(Files.readAllBytes(Paths.get(path)));
        return posTagging.statisticsTag(text, model.getLemma());
    }

    @Transactional
    public List<Word> statA(String sort) {
        List<Word> w = StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                .map(t -> Word.builder().word(t.getTagName()).frequency((long) t.getWords().size()).build())
                .collect(Collectors.toList());
        if (sort.equals("alphabetic")) {
            w.sort(Comparator.comparing(Word::getWord));
        } else if (sort.equals("frequency")) {
            w.sort(Comparator.comparing(Word::getFrequency));
        } else if (sort.equals("alphabetic_desc")) {
            w.sort(Comparator.comparing(Word::getFrequency).reversed());
        } else if (sort.equals("frequency_desc")) {
            w.sort(Comparator.comparing(Word::getFrequency).reversed());
        }
        return w;
    }

    public String save(Word model) {
        Map<String, WordEntity> words = new HashMap<>();
        Pattern pattern = Pattern.compile("\\w+_\\w+");
        Matcher matcher = pattern.matcher(model.getWord());
        while (matcher.find()) {
            String[] s = matcher.group().split("_");
            System.out.println(s[0] + " " + s[1]);
            if (words.containsKey(s[0])) {
                WordEntity w = words.get(s[0]);
                w.setFrequency(w.getFrequency() + 1);
                w.getTags().add(new WordTag(s[1]));
                words.put(s[0], w);
            } else {
                Set<WordTag> t = new HashSet<>();
                t.add(new WordTag(s[1]));
                words.put(s[0], WordEntity.builder()
                        .word(s[0])
                        .frequency(1L)
                        .tags(t)
                        .build());
            }
        }
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
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(wordEntity);
                }
            });
            wordRepository.saveAll(existingWords);
        }
        wordRepository.saveAll(words.values());
        return "";
    }

    public List<Word> search(String pattern, int page, int size) throws Exception {
        var s = posTagging.tag(pattern, "");
        var wl =  s.values().stream()
                .filter(d -> {
                    for (var t : d.getTags()) {
                        if (List.of("CD", "JJ", "JJS", "NN", "NNS", "NNP", "NNPS", "RB", "RBR", "RBS", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ").contains(t.getTagName())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
        var res = new ArrayList<Word>();
        for (var l : wl) {
            for (var x : texts.entrySet()) {
                for (var r : x.getValue()) {
                    if (r.getLemma().equalsIgnoreCase(l.getLemma())) {
                        String path = "texts/" + x.getKey();
                        String text = new String(Files.readAllBytes(Paths.get(path)));
                        var i = r.getTexts().get(0).getPos().intValue();
                        var i1 = i + 15 > text.length() - 1 ? text.length() - 1 : i + 15;
                        var i2 = i - 10 < 0 ? i : i - 10;
                        res.add(Word.builder().word(x.getKey()).frequency(r.getFrequency()).lemma(text.substring(i2, i1)).build());
                    }
                }
            }
        }
        var re = res.stream()
                .peek(r -> r.setFrequency(r.getFrequency() +
                        (long) Collections.frequency(res.stream()
                                        .map(Word::getWord)
                                        .collect(Collectors.toList()),
                                r.getWord()))).sorted(Comparator.comparingLong(Word::getFrequency).reversed()).collect(Collectors.toList());
        return re;
    }

    public List<Word> getList(Word model) throws IOException {
        return posTagging.getList(model.getWord());
    }
}
