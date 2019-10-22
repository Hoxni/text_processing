package com.example.ds1.postagging;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.entity.WordTag;
import com.example.ds1.repository.WordRepository;
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

@Service
public class POSTagging {

    private final WordRepository wordRepository;

    private POSTaggerME tagger = null;
    private POSModel model = null;

    public POSTagging(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public void initialize(String lexiconFileName) {
        try {
            InputStream modelStream = getClass().getResourceAsStream(lexiconFileName);
            model = new POSModel(modelStream);
            tagger = new POSTaggerME(model);
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
                    for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
                        String word = whitespaceTokenizerLine[i]
                                .trim()
                                .toLowerCase()
                                .replaceAll("[,.!?]+", "")
                                .replaceAll("[^\\w\\'\\-]", "")
                                .replaceAll("\\d+", "");
                        String tag = tags[i].trim()
                                .replaceAll("[,.!?]+", "");
                        if (words.containsKey(word)) {
                            WordEntity w = words.get(word);
                            w.setFrequency(w.getFrequency() + 1);
                            if (tag.matches("\\w+\\$*")) {
                                w.getTags().add(new WordTag(tag));
                                System.out.println(word + " " + tag);
                            }
                        } else {
                            Set<WordTag> wordTags = new HashSet<>();
                            if (tag.matches("\\w+\\$*")) {
                                wordTags.add(new WordTag(tag));
                                System.out.println(word + " " + tag);
                            }
                            words.put(word, WordEntity.builder()
                                    .word(word)
                                    .frequency(1L)
                                    .tags(wordTags)
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
}
