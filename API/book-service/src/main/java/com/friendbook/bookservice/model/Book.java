package com.friendbook.bookservice.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String originalName;

    @ManyToMany
    private Set<Author> authors;

    private Long year;

    private double rating;

    private String linkCover;

    @ManyToMany
    private Set<Genre> genres;

    @OneToMany
    private Set<Review> reviews;

    @ManyToMany
    private Set<Tag> tags;
}
