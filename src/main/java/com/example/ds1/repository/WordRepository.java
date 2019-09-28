package com.example.ds1.repository;

import com.example.ds1.entity.WordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends PagingAndSortingRepository<WordEntity, Long> {

    Optional<WordEntity> findByWord(String word);

    Optional<WordEntity> deleteByWord(String word);

    Page<WordEntity> findAllByWordLike(String pattern, Pageable pageable);

    List<WordEntity> findAllByWordIn(Collection<String> words);
}
