package com.example.ds1.postagging;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.entity.WordTag;
import com.example.ds1.model.Word;
import com.example.ds1.repository.WordRepository;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class POSTagging {

    private POSTaggerME tagger = null;
    private POSModel model = null;
    private DictionaryLemmatizer lemmatizer;

    public POSTagging(WordRepository wordRepository) {
    }

    public void initialize(String lexiconFileName) {
        try {
            InputStream modelStream = getClass().getResourceAsStream(lexiconFileName);
            model = new POSModel(modelStream);
            tagger = new POSTaggerME(model);
            InputStream dictLemmatizer = getClass().getResourceAsStream("/en-lemmatizer.dict");
            lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, WordEntity> tag(String text) {
        initialize("/en-pos-maxent.bin");
        try {
            if (model != null) {
                POSTaggerME tagger = new POSTaggerME(model);
                String[] sentences = detectSentences(text);
                Map<String, WordEntity> words = new HashMap<>();
                for (String sentence : sentences) {
                    String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                            .tokenize(sentence);
                    whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1] =
                            whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1]
                                    .replaceAll("[,.?!]+", "");

                    String[] tags = tagger.tag(whitespaceTokenizerLine);

                    String[] lemmas = lemmatizer.lemmatize(whitespaceTokenizerLine, tags);

                    for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
                        String word = whitespaceTokenizerLine[i]
                                .trim()
                                .toLowerCase()
                                .replaceAll("[,.!?]+", "")
                                .replaceAll("[^\\w\\'\\-]", "")
                                .replaceAll("'\\w+", "")
                                .replaceAll("\\w+'", "")
                                .replaceAll("\\d+", "");
                        String tag = tags[i].trim()
                                .replaceAll("[,.!?]+", "");
                        String lemma = !lemmas[i].equals("O") ? lemmas[i] : word;
                        if (words.containsKey(word) && !word.isEmpty()) {
                            WordEntity w = words.get(word);
                            w.setFrequency(w.getFrequency() + 1);
                            if (tag.matches("\\w+\\$*")) {
                                w.getTags().add(new WordTag(tag));
                                w.setLemma(lemma);
                                System.out.println(word + " " + tag + " " + lemma);
                            }
                        } else if (!word.isEmpty()) {
                            Set<WordTag> wordTags = new HashSet<>();
                            if (tag.matches("\\w+\\$*")) {
                                wordTags.add(new WordTag(tag));
                                System.out.println(word + " " + tag + " " + lemma);
                            }
                            words.put(word, WordEntity.builder()
                                    .word(word)
                                    .frequency(1L)
                                    .tags(wordTags)
                                    .lemma(lemma)
                                    .build());
                        }
                    }
                }
                return words;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] detectSentences(String paragraph) throws IOException {

        InputStream modelIn = getClass().getResourceAsStream("/en-sent.bin");
        final SentenceModel sentenceModel = new SentenceModel(modelIn);
        modelIn.close();

        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
        return sentenceDetector.sentDetect(paragraph);
    }

    public String text(String text) {
        initialize("/en-pos-maxent.bin");
        try {
            StringBuilder result = new StringBuilder();
            if (model != null) {
                POSTaggerME tagger = new POSTaggerME(model);
                String[] sentences = detectSentences(text);
                for (String sentence : sentences) {
                    String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                            .tokenize(sentence);
                    whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1] =
                            whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1]
                                    .replaceAll("[,.?!]+", "");

                    String[] tags = tagger.tag(whitespaceTokenizerLine);

                    for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
                        String word = whitespaceTokenizerLine[i];
                        String tag = tags[i];
                        result.append(word).append("_").append(tag).append(" ");
                    }
                }
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Word> statistics(String text, String sort) throws Exception {
        String res = text(text);
        Map<String, Long> stat = new HashMap<>();
        String[] sentences = detectSentences(res);
        for (String sentence : sentences) {
            String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                    .tokenize(sentence);
            whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1] =
                    whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1]
                            .replaceAll("[,.?!]+", "");

            for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
                String wordTag = whitespaceTokenizerLine[i].trim()
                        .toUpperCase()
                        .replaceAll("[,.!?]+", "")
                        .replaceAll("[^\\w\\'\\-]", "")
                        .replaceAll("'\\w+", "")
                        .replaceAll("\\w+'", "")
                        .replaceAll("\\d+", "");
                if (stat.containsKey(wordTag)) {
                    stat.put(wordTag, stat.get(wordTag) + 1);
                } else {
                    stat.put(wordTag, 1L);
                }
            }
        }
        List<Word> w = stat.entrySet().stream()
                .map(e -> Word.builder()
                        .word(e.getKey())
                        .frequency(e.getValue())
                        .build())
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

    public List<Word> statisticsTag(String text, String sort) throws Exception {
        initialize("/en-pos-maxent.bin");
        try {
            Map<String, Long> stat = new HashMap<>();
            if (model != null) {
                POSTaggerME tagger = new POSTaggerME(model);
                String[] sentences = detectSentences(text);
                for (String sentence : sentences) {
                    String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                            .tokenize(sentence);
                    whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1] =
                            whitespaceTokenizerLine[whitespaceTokenizerLine.length - 1]
                                    .replaceAll("[,.?!]+", "");

                    String[] tags = tagger.tag(whitespaceTokenizerLine);

                    for (int i = 0; i < whitespaceTokenizerLine.length - 1; i++) {
                        String t = tags[i] + "_" + tags[i + 1];
                        if (stat.containsKey(t)) {
                            stat.put(t, stat.get(t) + 1);
                        } else {
                            stat.put(t, 1L);
                        }
                    }
                }
            }
            List<Word> w = stat.entrySet().stream()
                    .map(e -> Word.builder()
                            .word(e.getKey())
                            .frequency(e.getValue())
                            .build())
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
