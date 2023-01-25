package com.friendbook.bookservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.friendbook.bookservice.DTO.AuthorForBook;
import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;
import com.friendbook.bookservice.model.Book;
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

    @Autowired
    UserBooksGradeService userBooksGradeService;

    @Autowired
    UserBooksWantToReadService userBooksWantToReadService;

    @Override
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Book not found.");
    }

    @Override
    public BookForBookPage getBookForBookPageById(Long id) {
        Optional<BookForBookPage> optionalBookForBookPage = bookRepository.getBookById(id);
        if (optionalBookForBookPage.isPresent()) {
            BookForBookPage newBookForBookPage = optionalBookForBookPage.get();
            bookRepository.updateCountRequestsById(newBookForBookPage.getId());
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
    public Set<BookForSearch> getBooksByAuthorId(Long authorId, Long userId) {
        Set<BookForSearch> books = bookRepository.getBooksByAuthorId(authorId);
        Set<BookForSearch> result = new HashSet<>();
        for (BookForSearch bookForSearch : books) {
            result.add(setInfoByBookForSearch(bookForSearch, userId));
        }
        if (!result.isEmpty()) {
            return result;
        }
        throw new EntityNotFoundException("Books not found.");
    }

    @Override
    public List<BookForSearch> getBooksBySearch(int numberPage,
                                                int sizePage,
                                                com.friendbook.bookservice.utils.Sort sort,
                                                String word,
                                                int startRating,
                                                int finishRating,
                                                List<Long> listTags,
                                                List<Long> listGenres, List<Long> listId, Long userId) {
        Page<BookForSearch> page;
        Integer countOfTags = listTags == null || listTags.isEmpty() ? 0 : listTags.size();
        Integer countOfGenres = listGenres == null || listGenres.isEmpty() ? 0 : listGenres.size();
        if (sort == com.friendbook.bookservice.utils.Sort.Popularity) {
            page = bookRepository.getBooksBySearch(
                    startRating, finishRating, word, listId, listGenres, countOfGenres, listTags, countOfTags,
                    PageRequest.of(numberPage, sizePage, Sort.by("countRequests").descending()));
        } else if (sort == com.friendbook.bookservice.utils.Sort.Rating) {
            page = bookRepository.getBooksBySearch(
                    startRating, finishRating, word, listId, listGenres, countOfGenres, listTags, countOfTags,
                    PageRequest.of(numberPage, sizePage, JpaSort.unsafe(Sort.Direction.DESC, "(b.sumMarks + 0.0) / (b.countMarks + 0.0)")));
        } else {
            page = bookRepository.getBooksBySearch(
                    startRating, finishRating, word, listId, listGenres, countOfGenres, listTags, countOfTags,
                    PageRequest.of(numberPage, sizePage));
        }

        List<BookForSearch> books = new ArrayList<>();
        if (page != null) {
            books = page.get().toList();

            for (BookForSearch book : books) {
                if (book.getAuthors() != null) {
                    for (int i = 0; i < book.getAuthors().size(); i++) {
                        AuthorForBook newAuthor = authorRestTemplateClient
                                .getAuthorById(book.getAuthors().get(i).getId());
                        book.getAuthors().set(i, newAuthor);
                    }
                }
                if (book.getGenres() != null) {
                    for (int i = 0; i < book.getGenres().size(); i++) {
                        book.getGenres()
                                .set(i, genreService.getGenreById(book.getGenres().get(i).getId()));
                    }
                }
            }
        }
        List<BookForSearch> result = new ArrayList<>();
        for (BookForSearch bookForSearch : books) {
            result.add(setInfoByBookForSearch(bookForSearch, userId));
        }
        if (!result.isEmpty()) {
            return result;
        }
        throw new EntityNotFoundException("Books not found.");
    }

    @Override
    public void updateCountMarksAndSumMarks(Book book, int differenceSum) {
        if (differenceSum > 0) {
            book.setCountMarks(book.getCountMarks() + 1);
        } else {
            book.setCountMarks(book.getCountMarks() - 1);
        }
        book.setSumMarks(book.getSumMarks() + differenceSum);
        bookRepository.save(book);
    }

    @Override
    public Set<BookForSearch> getBooksByBooksId(List<Long> listId, Long userId) {
        Set<BookForSearch> books = bookRepository.getBooksByBooksId(listId);
        Set<BookForSearch> result = new HashSet<>();
        for (BookForSearch bookForSearch : books) {
            result.add(setInfoByBookForSearch(bookForSearch, userId));
        }
        if (!result.isEmpty()) {
            return result;
        }
        throw new EntityNotFoundException("Books not found.");
    }

    BookForSearch setInfoByBookForSearch(BookForSearch bookForSearch, Long userId) {
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
        if (userId > 0) {
            try {
                bookForSearch.setGrade(userBooksGradeService.getGradeByBookIdAndUserId(bookForSearch.getId(), userId));
            } catch (EntityNotFoundException exception) {
                bookForSearch.setGrade(null);
            }
            bookForSearch.setIsWantToRead(userBooksWantToReadService.isSavingBook(bookForSearch.getId(), userId));
        } else {
            bookForSearch.setGrade(null);
            bookForSearch.setIsWantToRead(false);
        }
        return bookForSearch;
    }
}
