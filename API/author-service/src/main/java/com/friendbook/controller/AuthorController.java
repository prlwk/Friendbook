package com.friendbook.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.service.AuthorService;
import com.friendbook.utils.AppError;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(authorService.getAuthor(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Author with id:" + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/for-book/{id}")
    public ResponseEntity<?> getAuthorForBook(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(authorService.getAuthorForBook(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Author with id:" + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getAuthorsByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(authorService.getAuthorsByAuthorName(name), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Authors with name:" + name + " not found."), HttpStatus.NOT_FOUND);
        }
    }
}
