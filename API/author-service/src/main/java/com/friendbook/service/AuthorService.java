package com.friendbook.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.DTO.AuthorForSearch;
import com.friendbook.DTO.AuthorWithBooks;
import com.friendbook.model.Author;
import com.friendbook.utils.Sort;

public interface AuthorService {
    Author getAuthor(Long id);

    AuthorWithBooks getAuthorWithBooks(Long authorId);

    AuthorForBook getAuthorForBook(Long authorId);

    List<AuthorForSearch> getAuthorsByAuthorName(String name);

    List<AuthorForSearch> getAllAuthorsForSearch();

    Page<AuthorForSearch> search(int numberPage, int sizePage, Sort sort, String word, int startRating, int finishRating);
}
