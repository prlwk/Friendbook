package com.friendbook.bookservice.DTO;

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
}
