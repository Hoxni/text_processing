package com.example.ds1.repository;

import com.example.ds1.entity.WordEntity;
import com.example.ds1.entity.WordTag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends PagingAndSortingRepository<WordTag, Long> {
}
