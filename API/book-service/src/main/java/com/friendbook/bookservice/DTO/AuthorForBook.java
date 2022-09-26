package com.friendbook.bookservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorForBook {
    private Long id;
    private String name;

    public AuthorForBook(Long id) {
        this.id = id;
    }
}
