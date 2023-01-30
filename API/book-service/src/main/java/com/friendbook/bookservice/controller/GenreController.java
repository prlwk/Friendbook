package com.friendbook.bookservice.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.bookservice.service.GenreService;
import com.friendbook.bookservice.utils.AppError;

@RestController
@RequestMapping("/genre")
public class GenreController {

    private GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllGenres() {
        try {
            return new ResponseEntity<>(genreService.getAllGenres(), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "There are no genres."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularGenres() {
        try {
            return new ResponseEntity<>(genreService.getPopularGenres(), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "There are no popular genres."), HttpStatus.NOT_FOUND);
        }
    }
}
