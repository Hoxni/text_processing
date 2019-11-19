package com.example.ds1.mapper;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.entity.WordTag;
import com.example.ds1.model.Word;

import java.util.Set;
import java.util.stream.Collectors;

public class WordMapper {
    public static Word toModel(WordEntity entity, Set<String> tags) {
        return Word.builder()
                .word(entity.getWord())
                .frequency(entity.getFrequency())
                .tags(entity.getTags().stream()
                        .map(WordTag::getTagName)
                        .collect(Collectors.toSet()))
                .lemma(entity.getLemma())
                .lemmaTags(tags)
                .build();
    }

    public static WordEntity toEntity(Word model) {
        return WordEntity.builder()
                .word(model.getWord())
                .frequency(model.getFrequency()).build();
    }
}
