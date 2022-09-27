package com.friendbook.bookservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagForBook {
    private Long id;

    private String name;

    public TagForBook(Long id) {
        this.id = id;
    }
}
