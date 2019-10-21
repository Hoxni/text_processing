package com.example.ds1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "words")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String word;

    @Getter
    @Setter
    private Long frequency;

    @Getter
    @ManyToMany
    @JoinTable(
            name = "word_tag",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_tag_name"))
    @ElementCollection(targetClass = WordTag.class)
    private Set<WordTag> tags;
}
