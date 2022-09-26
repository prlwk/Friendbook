package com.friendbook.bookservice.service;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.bookservice.DTO.AuthorForBook;
import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.repository.BookRepository;
import com.friendbook.bookservice.service.client.AuthorRestTemplateClient;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRestTemplateClient authorRestTemplateClient;

    @Override
    public Book getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
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
        for (BookForSearch bookForSearch: books) {
            for (int i = 0; i < bookForSearch.getAuthors().size(); i++) {
                AuthorForBook newAuthor = authorRestTemplateClient.getAuthorById(bookForSearch.getAuthors().get(i).getId());
                System.out.println(bookForSearch.getAuthors().get(i));
                System.out.println(newAuthor.getId());
                System.out.println(newAuthor.getName());
                bookForSearch.getAuthors().set(i, newAuthor);
            }
        }
        if (!books.isEmpty()) {
            return books;
        }
        throw new EntityNotFoundException("Books not found.");
    }
}
