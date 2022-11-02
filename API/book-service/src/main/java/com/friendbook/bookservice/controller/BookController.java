package com.friendbook.bookservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

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

import com.friendbook.bookservice.DTO.AuthorForBook;
import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.service.BookService;
import com.friendbook.bookservice.service.client.AuthorRestTemplateClient;
import com.friendbook.bookservice.service.client.UserRestTemplateClient;
import com.friendbook.bookservice.utils.AppError;
import com.friendbook.bookservice.utils.Sort;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRestTemplateClient authorRestTemplateClient;

    @Autowired
    private UserRestTemplateClient userRestTemplateClient;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookService.getBookForBookPageById(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Book with id: " + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-author-id-for-author-page/{id}")
    public ResponseEntity<?> getBooksByAuthorIdForAuthorPage(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookService.getBooksByAuthorIdForAuthorPage(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Books for author with id: " + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-author-id/{id}")
    public ResponseEntity<?> getBooksByAuthorId(@PathVariable Long id, HttpServletRequest request) {
        boolean isAuthUser = false;
        if (userRestTemplateClient.checkToken(request)) {
            isAuthUser = true;
        }
        try {
            return new ResponseEntity<>(bookService.getBooksByAuthorId(id, isAuthUser, request), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Books for author with id: " + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getBooksByAuthorId(
            @RequestParam int numberPage,
            @RequestParam int sizePage,
            @RequestParam(required = false) String word,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer startRating,
            @RequestParam(required = false, defaultValue = "10") Integer finishRating,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String genres, HttpServletRequest request) {
        Sort sortType = Sort.Nothing;
        if (sort != null) {
            if (sort.equals("popularity")) {
                sortType = Sort.Popularity;
            } else if (sort.equals("rating")) {
                sortType = Sort.Rating;
            }
        }
        Scanner scanner = new Scanner("");
        List<Long> listTags = new ArrayList<>();
        if (tags != null) {
            scanner = new Scanner(tags);
            scanner.useDelimiter(",");
            while (scanner.hasNextLong()) {
                listTags.add(scanner.nextLong());
            }
        }
        List<Long> listGenres = new ArrayList<>();
        if (genres != null) {
            scanner = new Scanner(genres);
            scanner.useDelimiter(",");
            while (scanner.hasNextLong()) {
                listGenres.add(scanner.nextLong());
            }
        }
        scanner.close();

        List<Long> listId = new ArrayList<>();
        if (word != null) {
            List<AuthorForBook> authorForBookList = authorRestTemplateClient.getAuthorsByName(word);
            if (authorForBookList != null) {
                listId = authorRestTemplateClient.getAuthorsByName(word)
                        .stream().map(AuthorForBook::getId).collect(Collectors.toList());
            }
        }
        try {
            return new ResponseEntity<>(
                    bookService.getBooksBySearch(
                            numberPage,
                            sizePage,
                            sortType,
                            word,
                            startRating,
                            finishRating,
                            listTags,
                            listGenres,
                            listId), HttpStatus.OK);
        } catch (
                EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "There are no such books."), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<?> getBookImage(@RequestParam Long id) {
        Book book;
        try {
            book = bookService.getBookById(id);
        } catch (EntityNotFoundException exception) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Book with id " + id + " does not exist."), httpHeaders, HttpStatus.NOT_FOUND);
        }

        try {
            URL res = getClass().getClassLoader().getResource("images/" + book.getLinkCover());
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
