package com.friendbook.bookservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreForBook {
    private Long id;

    private String name;

    public GenreForBook(Long id) {
        this.id = id;
    }
}
