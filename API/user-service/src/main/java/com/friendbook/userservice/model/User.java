package com.friendbook.userservice.model;

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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String login;

    private String linkPhoto;

    @ManyToMany
    private Set<User> friends;

    @ManyToMany
    private Set<BookRate> booksRate;

    @OneToMany
    private Set<Review> review;

    @ManyToMany
    private Set<BookWantToRead> booksWantToRead;

}
