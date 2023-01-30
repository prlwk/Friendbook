package com.friendbook.bookservice.service;

import java.util.List;

import com.friendbook.bookservice.DTO.GenreForBook;
import com.friendbook.bookservice.model.Genre;

public interface GenreService {
    GenreForBook getGenreById(Long id);
    List<GenreForBook> getAllGenres();
    Genre getGenreByName(String name);
    List<GenreForBook> getPopularGenres();
}
