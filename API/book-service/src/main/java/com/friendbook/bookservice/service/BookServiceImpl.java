package com.friendbook.bookservice.service;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.bookservice.DTO.AuthorForBook;
import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.repository.BookRepository;
import com.friendbook.bookservice.service.client.AuthorRestTemplateClient;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    GenreService genreService;

    @Autowired
    TagService tagService;

    @Autowired
    AuthorRestTemplateClient authorRestTemplateClient;

    @Override
    public BookForBookPage getBookById(Long id) {
        Optional<BookForBookPage> optionalBookForBookPage = bookRepository.getBookById(id);
        if (optionalBookForBookPage.isPresent()) {
            BookForBookPage newBookForBookPage = optionalBookForBookPage.get();
            if (newBookForBookPage.getAuthors() != null) {
                for (int i = 0; i < newBookForBookPage.getAuthors().size(); i++) {
                    AuthorForBook newAuthor =
                            authorRestTemplateClient.getAuthorById(newBookForBookPage.getAuthors().get(i).getId());
                    newBookForBookPage.getAuthors().set(i, newAuthor);
                }
            }
            if (newBookForBookPage.getGenres() != null) {
                for (int i = 0; i < newBookForBookPage.getGenres().size(); i++) {
                    newBookForBookPage
                            .getGenres()
                            .set(i, genreService.getGenreById(newBookForBookPage.getGenres().get(i).getId()));
                }
            }
            if (newBookForBookPage.getTags() != null) {
                for (int i = 0; i < newBookForBookPage.getTags().size(); i++) {
                    newBookForBookPage
                            .getTags()
                            .set(i, tagService.getTagById(newBookForBookPage.getTags().get(i).getId()));
                }
            }
            return optionalBookForBookPage.get();
        }
        throw new EntityNotFoundException("Book not found.");
    }

    @Override
    public Set<BookForAuthor> getBooksByAuthorIdForAuthorPage(Long authorId) {
        Set<BookForAuthor> books = bookRepository.getBooksByAuthorIdForAuthorPage(authorId);
        if (books != null && !books.isEmpty()) {
            return books;
        }
        throw new EntityNotFoundException("Books not found.");
    }

    @Override
    public Set<BookForSearch> getBooksByAuthorId(Long authorId) {
        Set<BookForSearch> books = bookRepository.getBooksByAuthorId(authorId);
        for (BookForSearch bookForSearch : books) {
            if (bookForSearch.getAuthors() != null) {
                for (int i = 0; i < bookForSearch.getAuthors().size(); i++) {
                    AuthorForBook newAuthor = authorRestTemplateClient.getAuthorById(bookForSearch.getAuthors().get(i).getId());
                    bookForSearch.getAuthors().set(i, newAuthor);
                }
            }
            if (bookForSearch.getGenres() != null) {
                for (int i = 0; i < bookForSearch.getGenres().size(); i++) {
                    bookForSearch
                            .getGenres()
                            .set(i, genreService.getGenreById(bookForSearch.getGenres().get(i).getId()));
                }
            }
        }

        if (!books.isEmpty()) {
            return books;
        }
        throw new EntityNotFoundException("Books not found.");
    }
}
