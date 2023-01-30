package com.friendbook.bookservice.service;

import java.util.ArrayList;
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

    @Override
    public Genre getGenreByName(String name) {
        Optional<Genre> genreOptional = genreRepository.getByName(name);
        if (genreOptional.isPresent()) {
            return genreOptional.get();
        }
        throw new EntityNotFoundException("Genre not found.");
    }

    @Override
    public List<GenreForBook> getPopularGenres() throws EntityNotFoundException{
        List<GenreForBook> list = new ArrayList<>();
        Genre genre = getGenreByName("Фантастика");
        list.add(new GenreForBook(genre.getId(), genre.getName()));
        genre = getGenreByName("Детектив");
        list.add(new GenreForBook(genre.getId(), genre.getName()));
        genre = getGenreByName("Приключения");
        list.add(new GenreForBook(genre.getId(), genre.getName()));
        genre = getGenreByName("Роман");
        list.add(new GenreForBook(genre.getId(), genre.getName()));
        genre = getGenreByName("Триллер");
        list.add(new GenreForBook(genre.getId(), genre.getName()));
        genre = getGenreByName("Психология");
        list.add(new GenreForBook(genre.getId(), genre.getName()));
        return list;
    }
}
