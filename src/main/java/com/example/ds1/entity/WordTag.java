package com.example.ds1.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Entity
@Table(name = "tags")
public class WordTag {
    public WordTag(String tagName) {
        this.tagName = tagName;
    }

    @Getter
    @Id
    @Column(name = "tag_name")
    private String tagName;

    @Getter
    @ManyToMany
    @JoinTable(
            name = "word_tag",
            joinColumns = @JoinColumn(name = "tag_tag_name"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    @ElementCollection(targetClass = WordEntity.class)
    private Set<WordEntity> words;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordTag wordTag = (WordTag) o;
        return Objects.equals(tagName, wordTag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }
}
