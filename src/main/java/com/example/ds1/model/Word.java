package com.example.ds1.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Word {
    private String word;
    private Long frequency;
}
