package com.friendbook.userservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_books_want_to_read")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBooksWantToRead {
    @Id
    @Column(name = "idBook", nullable = false)
    private Long bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
