package com.friendbook.bookservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.bookservice.model.Genre;

@RestController
@RequestMapping("/genre")
public class GenreController {

    @GetMapping("/all")
    public ResponseEntity<Genre> getAllGenres() {
        return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }
}
