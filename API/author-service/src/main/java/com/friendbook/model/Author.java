package com.friendbook.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String originalName;

    private String yearsLife;

    @ManyToMany
    private Set<Book> books;

    private double rating;

    private String photoSrc;

    private String biography;

}
