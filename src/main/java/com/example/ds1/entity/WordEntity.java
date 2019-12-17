package com.example.ds1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "words")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(name = "word", nullable = false, unique = true)
    private String word;

    @Getter
    @Setter
    private Long frequency;

    @Getter
    @Setter
    private String lemma;

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
            name = "word_tag",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_tag_name"))
    @ElementCollection(targetClass = WordTag.class)
    private Set<WordTag> tags;

    @Getter
    @Setter
    @ElementCollection
    @CollectionTable(name = "word_text", joinColumns = @JoinColumn(name = "word_id"), foreignKey = @ForeignKey(name = "WORD_TEXT_WORDS_ID_FK"))
    private List<Text> texts;


}
