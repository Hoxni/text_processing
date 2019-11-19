package com.example.ds1.repository;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.entity.WordTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WordRepository extends PagingAndSortingRepository<WordEntity, Long> {

    Optional<WordEntity> findByWord(String word);

    Integer deleteByWord(String word);

    Page<WordEntity> findAllByWordLike(String pattern, Pageable pageable);

    List<WordEntity> findAllByWordIn(Collection<String> words);

    @Query(value = "select TAGS.* " +
            "from TAGS " +
            "join WORD_TAG WT on TAGS.TAG_NAME = WT.TAG_TAG_NAME " +
            "join WORDS W on WT.WORD_ID = W.ID " +
            "where W.WORD = :lemma ", nativeQuery = true)
    Set<WordTag> getTagsByWord(@Param("lemma") String lemma);
}
