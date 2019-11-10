package com.example.ds1.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Word {
    private String word;
    private Long frequency;
    private Set<String> tags;
    private String lemma;
}
