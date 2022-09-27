package com.friendbook.bookservice.DTO;

import java.util.ArrayList;
import java.util.List;

import com.friendbook.bookservice.model.Book;
import com.friendbook.bookservice.model.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookForBookPage {
    private Long id;

    private String name;

    private List<AuthorForBook> authors;

    private Long year;

    private double rating;

    private String linkCover;

    private String description;

    private List<GenreForBook> genres;

    private List<Review> reviews;

    private List<TagForBook> tags;

    public BookForBookPage(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.authors = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.genres = new ArrayList<>();
        book.getAuthors().forEach(a -> this.authors.add(new AuthorForBook(a.getId())));
        book.getGenres().forEach(g -> this.genres.add(new GenreForBook(g.getId())));
        book.getTags().forEach(t -> this.tags.add(new TagForBook(t.getId())));
        this.year = book.getYear();
        this.rating = book.getRating();
        this.linkCover = book.getLinkCover();
        this.description = book.getDescription();
    }
}
