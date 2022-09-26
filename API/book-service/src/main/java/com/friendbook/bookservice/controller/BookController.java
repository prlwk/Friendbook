package com.friendbook.bookservice.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.service.BookService;
import com.friendbook.bookservice.utils.AppError;

@RestController
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                    "Book with id:" + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-author-id-for-author-page/{id}")
    public ResponseEntity<?> getBooksByAuthorIdForAuthorPage(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookService.getBooksByAuthorIdForAuthorPage(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Books for author with id:" + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-author-id/{id}")
    public ResponseEntity<?> getBooksByAuthorId(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookService.getBooksByAuthorId(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Books for author with id:" + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Book> addBook() {
        return new ResponseEntity<Book>(HttpStatus.SEE_OTHER);
    }
}
