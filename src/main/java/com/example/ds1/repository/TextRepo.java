package com.example.ds1.repository;

import com.example.ds1.entity.Text;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepo extends PagingAndSortingRepository<Text, Long> {

}
