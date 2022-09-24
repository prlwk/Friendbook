package com.friendbook.userservice.model;

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
@Table(name = "books_want_to_read")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookWantToRead {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(mappedBy = "booksWantToRead")
    private Set<User> users;

}
