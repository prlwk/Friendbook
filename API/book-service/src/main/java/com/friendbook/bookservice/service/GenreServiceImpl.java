package com.friendbook.bookservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.bookservice.DTO.GenreForBook;
import com.friendbook.bookservice.model.Genre;
import com.friendbook.bookservice.repository.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    GenreRepository genreRepository;

    @Override
    public GenreForBook getGenreById(Long id) {
        Optional<Genre> optionalGenreForBook = genreRepository.findById(id);
        if (optionalGenreForBook.isPresent()) {
            return new GenreForBook(optionalGenreForBook.get().getId(), optionalGenreForBook.get().getName());
        }
        throw new EntityNotFoundException("Genre not found.");
    }

    @Override
    public List<GenreForBook> getAllGenres() {
        List<GenreForBook> genres = genreRepository.getAll();
        if (genres != null && !genres.isEmpty()) {
            return genres;
        }
        throw new EntityNotFoundException("Tags not found.");
    }
}
