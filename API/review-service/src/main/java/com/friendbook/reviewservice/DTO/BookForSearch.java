package com.friendbook.reviewservice.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookForSearch {
    private Long id;

    private String name;

    private List<AuthorForBook> authors;

    private Long year;

    private double rating;

    private String linkCover;

    private List<GenreForBook> genres;

    private Integer grade;

    private Boolean isWantToRead;
}
