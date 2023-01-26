package com.friendbook.DTO;

import com.friendbook.model.Author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorForSearch {
    private Long id;

    private String name;

    private String photoSrc;

    private Double rating;

    private String yearsLife;

    public AuthorForSearch(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.photoSrc = author.getPhotoSrc();
        this.yearsLife = author.getYearsLife();
    }
}
