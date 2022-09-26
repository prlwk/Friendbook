package com.friendbook.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {
    private Long id;

    private double rating;

    private String linkCover;
}
