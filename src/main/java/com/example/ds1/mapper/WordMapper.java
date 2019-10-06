package com.example.ds1.mapper;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.model.Word;

public class WordMapper {
    public static Word toModel(WordEntity entity) {
        return Word.builder()
                .word(entity.getWord())
                .frequency(entity.getFrequency())
                .build();
    }

    public static WordEntity toEntity(Word model) {
        return WordEntity.builder()
                .word(model.getWord())
                .frequency(model.getFrequency()).build();
    }
}
