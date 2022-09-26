package com.friendbook.bookservice.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.util.Pair;

import com.friendbook.bookservice.model.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookForSearch {
    private Long id;

    private String name;

    private List<AuthorForBook> authors;

    private Long year;

    private double rating;

    private String linkCover;

    private Set<Pair<Long, String>> genres;


    public BookForSearch(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.authors = new ArrayList<>();
        book.getAuthors().forEach(a -> this.authors.add(new AuthorForBook(a.getId())));
        this.year = book.getYear();
        this.rating = book.getRating();
        this.linkCover = book.getLinkCover();
        this.genres = new HashSet<>();
    }
}
