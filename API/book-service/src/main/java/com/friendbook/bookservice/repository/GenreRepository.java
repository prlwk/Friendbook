package com.friendbook.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.friendbook.bookservice.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
