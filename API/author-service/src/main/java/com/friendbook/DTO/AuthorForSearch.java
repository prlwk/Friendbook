package com.friendbook.DTO;

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
}
