package com.friendbook.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friendbook.bookservice.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
