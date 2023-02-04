package com.friendbook.bookservice.DTO;

import java.util.ArrayList;
import java.util.List;

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

    private String year;

    private double rating;

    private String linkCover;

    private List<GenreForBook> genres;

    private Integer grade;

    private Boolean isWantToRead;


    public BookForSearch(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
        book.getAuthors().forEach(a -> this.authors.add(new AuthorForBook(a.getId())));
        book.getGenres().forEach(g -> this.genres.add(new GenreForBook(g.getId())));
        this.year = book.getYear();
        if (book.getCountMarks() != 0) {
            this.rating = (double) book.getSumMarks() / book.getCountMarks();
        } else {
            this.rating = 0;
        }
        this.linkCover = "https://disk.yandex.ru/d/bgISdzmB8PYa1A/" + book.getLinkCover();
    }
}
