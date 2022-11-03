package com.friendbook.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.DTO.AuthorWithBooks;
import com.friendbook.model.Author;
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
            return new ResponseEntity<>(authorService.getAuthorWithBooks(id), HttpStatus.OK);
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

    @RequestMapping(path = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAuthorImage(@RequestParam Long id) {
        Author author;
        try {
            author = authorService.getAuthor(id);
        } catch (EntityNotFoundException exception) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Author with id " + id + " does not exist."), httpHeaders, HttpStatus.NOT_FOUND);
        }

        try {
            URL res = getClass().getClassLoader().getResource("images/" + author.getPhotoSrc());
            File file;
            if (res != null) {
                file = Paths.get(res.toURI()).toFile();
                InputStream input = new FileInputStream(file);
                return new ResponseEntity<>(IOUtils.toByteArray(input), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
