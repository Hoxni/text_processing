package com.example.ds1.controller;

import com.example.ds1.model.ExtractRequestModel;
import com.example.ds1.model.Word;
import com.example.ds1.service.WordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/words")
    public ResponseEntity<List<Word>> getAllWords(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "100") @Max(200) int size,
            @RequestParam(defaultValue = "alpha") String sort) {
        return ResponseEntity.ok(wordService.getAllWords(page, size, sort));
    }

    @PostMapping("/words")
    public ResponseEntity addWord(@RequestBody Word word) {
        wordService.addWord(word);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/words/find")
    public ResponseEntity<List<Word>> findWordsWithPattern(
            @RequestParam String pattern,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "100") @Max(200) int size,
            @RequestParam(defaultValue = "alphabetic") String sort) {
        return ResponseEntity.ok(wordService.findWordsWithPattern(pattern, page, size, sort));
    }

    @PostMapping("/words/extract")
    public ResponseEntity extractWords(@RequestBody String model) throws Exception {
        wordService.extractWords(model);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/words/{word}")
    public ResponseEntity<Word> changeWord(@PathVariable String word, @RequestBody Word model) {
        return ResponseEntity.ok(wordService.changeWord(word, model));
    }

    @DeleteMapping("/words/{word}")
    public ResponseEntity<Integer> deleteWord(@PathVariable String word) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(wordService.deleteWord(word));
    }
}
