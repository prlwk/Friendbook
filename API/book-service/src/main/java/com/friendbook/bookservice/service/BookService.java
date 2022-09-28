package com.friendbook.bookservice.service;


import java.util.List;
import java.util.Set;

import com.friendbook.bookservice.DTO.BookForAuthor;
import com.friendbook.bookservice.DTO.BookForBookPage;
import com.friendbook.bookservice.DTO.BookForSearch;

public interface BookService {
    public BookForBookPage getBookById(Long id);
    public Set<BookForAuthor> getBooksByAuthorIdForAuthorPage(Long authorId);
    public Set<BookForSearch> getBooksByAuthorId(Long authorId);
    public List<BookForSearch> getBooksBySearch(int numberPage,
                                                int sizePage,
                                                com.friendbook.bookservice.utils.Sort sort,
                                                String word,
                                                int startRating,
                                                int finishRating,
                                                List<Long> listTags,
                                                List<Long> listGenres,
                                                List<Long> listId);
}
