package com.friendbook.bookservice.DTO;

import com.friendbook.bookservice.model.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookForAuthor {
    private Long id;

    private double rating;

    private String linkCover;

    public BookForAuthor(Book book) {
        this.id = book.getId();
        if (book.getCountMarks() != 0) {
            this.rating = (double) book.getSumMarks() / book.getCountMarks();
        } else {
            this.rating = 0;
        }
        this.linkCover = "https://disk.yandex.ru/d/bgISdzmB8PYa1A/" + book.getLinkCover();
    }
}
