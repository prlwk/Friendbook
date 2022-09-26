package com.friendbook.DTO;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorWithBooks {
    private Long id;

    private String name;

    private String yearsLife;

    private Set<Book> books;

    private double rating;

    private String photoSrc;

    private String biography;
}
