package com.friendbook.bookservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
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
import org.springframework.web.client.HttpClientErrorException;

import com.friendbook.bookservice.DTO.AuthorForSearch;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.service.BookService;
import com.friendbook.bookservice.service.UserBooksGradeService;
import com.friendbook.bookservice.service.UserBooksWantToReadService;
import com.friendbook.bookservice.service.client.AuthorRestTemplateClient;
import com.friendbook.bookservice.service.client.ReviewRestTemplateClient;
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
    private ReviewRestTemplateClient reviewRestTemplateClient;

    @Autowired
    private UserRestTemplateClient userRestTemplateClient;

    @Autowired
    private UserBooksGradeService userBooksGradeService;

    @Autowired
    private UserBooksWantToReadService userBooksWantToReadService;

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
        Long userId = (long) -1;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException | HttpClientErrorException.Forbidden exception) {
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }
        try {
            return new ResponseEntity<>(bookService.getBooksByAuthorId(id, userId), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Books for author with id: " + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getBooksBySearch(
            @RequestParam int numberPage,
            @RequestParam int sizePage,
            @RequestParam(required = false) String word,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer startRating,
            @RequestParam(required = false, defaultValue = "10") Integer finishRating,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String genres, HttpServletRequest request) {
        Long userId = (long) -1;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException | HttpClientErrorException.Forbidden exception) {
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }
        Map<String, List> result = new HashMap<>();
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

        List<Long> listAuthorId;
        List<Long> listBookId = new ArrayList<>();
        if (word != null) {
            List<AuthorForSearch> authorForBookList = authorRestTemplateClient.getAuthorsByName(word);
            if (authorForBookList != null) {
                listAuthorId = authorForBookList.stream().map(AuthorForSearch::getId).collect(Collectors.toList());
                for (Long authorId : listAuthorId) {
                    try {
                        bookService.getBooksByAuthorIdForAuthorPage(authorId)
                                .forEach(x -> listBookId.add(x.getId()));
                    } catch (EntityNotFoundException ignored) {
                    }
                }
                result.put("authors", authorForBookList);
            }
        }
        try {
            System.out.println(listBookId.size());
            result.put("books", bookService.getBooksBySearch(numberPage, sizePage, sortType, word, startRating,
                    finishRating, listTags, listGenres, listBookId, userId));
        } catch (EntityNotFoundException entityNotFoundException) {
            if (!result.containsKey("authors")) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "There are no such results."), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
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

    @RequestMapping(path = "/set-grade-and-review", method = RequestMethod.GET)
    public ResponseEntity<?> setGrade(@RequestParam(required = false) Integer grade, @RequestParam Long idBook, @RequestParam(required = false) String review, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can set grade"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        }
        Book book;
        try {
            book = bookService.getBookById(idBook);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Book with id " + idBook + " does not exist."), HttpStatus.NOT_FOUND);
        }
        if (grade != null && (grade > 10 || grade < 1)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Grade must be more between 1 and 10"), HttpStatus.BAD_REQUEST);
        }
        try {
            int gradeByBookAndUserId = userBooksGradeService.getGradeByBookIdAndUserId(idBook, userId);
            bookService.updateCountMarksAndSumMarks(book, -1 * gradeByBookAndUserId);
        } catch (EntityNotFoundException ignored) {
        }
        userBooksGradeService.setGrade(book, grade, userId);
        try {
            if (grade != null) {
                bookService.updateCountMarksAndSumMarks(book, grade);
                if (review != null) {
                    reviewRestTemplateClient.setReview(review, grade, idBook, request);
                } else {
                    reviewRestTemplateClient.deleteReview(idBook, request);
                }
            } else {
                reviewRestTemplateClient.deleteReview(idBook, request);
            }
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with review service"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can write review"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException.BadRequest exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Error connecting to another book service instance"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/grade", method = RequestMethod.GET)
    public ResponseEntity<?> getGrade(@RequestParam Long idBook, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can set grade"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        try {
            bookService.getBookById(idBook);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Book with id " + idBook + " does not exist."), HttpStatus.NOT_FOUND);
        }
        try {
            userBooksGradeService.getGradeByBookIdAndUserId(idBook, userId);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/save-book", method = RequestMethod.GET)
    public ResponseEntity<?> saveBook(@RequestParam Long idBook, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can save book"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        Book book;
        try {
            book = bookService.getBookById(idBook);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        userBooksWantToReadService.saveBook(book, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-saving-book", method = RequestMethod.GET)
    public ResponseEntity<?> deleteSavingBook(@RequestParam Long idBook, HttpServletRequest request) {
        Long userId;
        try {
            userId = userRestTemplateClient.getUserId(request);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lost connection with user service"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException.Forbidden exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.FORBIDDEN.value(),
                            "Only authorized user can delete saving book"), HttpStatus.FORBIDDEN);
        } catch (HttpClientErrorException.Unauthorized exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "Access token is expired"), HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        Book book;
        try {
            book = bookService.getBookById(idBook);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        try {
            userBooksWantToReadService.deleteSavingBook(book, userId);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/rated-books", method = RequestMethod.GET)
    public ResponseEntity<?> getRatedBooks(@RequestParam Long userId) {
        try {
            Set<BookForSearch> set =
                    bookService.getBooksByBooksId(userBooksGradeService.getRatedBooksIdByUserId(userId), userId);
            return new ResponseEntity<>(set, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/saving-books", method = RequestMethod.GET)
    public ResponseEntity<?> getSavingBooks(@RequestParam Long userId) {
        try {
            Set<BookForSearch> set =
                    bookService.getBooksByBooksId(userBooksWantToReadService.getSavingBooksIdByUserId(userId), userId);
            return new ResponseEntity<>(set, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
