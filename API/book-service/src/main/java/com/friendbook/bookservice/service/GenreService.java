package com.friendbook.bookservice.service;

import java.util.List;

import com.friendbook.bookservice.DTO.GenreForBook;

public interface GenreService {
    public GenreForBook getGenreById(Long id);
    public List<GenreForBook> getAllGenres();
}
