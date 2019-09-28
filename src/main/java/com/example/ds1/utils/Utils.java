package com.example.ds1.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Utils {
    public static Long zeroIfNull(Long value) {
        return value == null ? 0L : value;
    }

    public static Pageable createPageable(int page, int size, String sort){
        return PageRequest.of(page - 1, size, sortType(sort));
    }

    public static Sort sortType(String sort) {
        switch (sort) {
            case "alphabetic":
                return Sort.by("word");
            case "alphabetic_desc":
                return Sort.by(Sort.Direction.DESC, "word");
            case "frequency":
                return Sort.by("frequency");
            case "frequency_desc":
                return Sort.by(Sort.Direction.DESC, "frequency");
            default:
                return Sort.by("word");

        }
    }
}
