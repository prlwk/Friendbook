package com.friendbook.bookservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.bookservice.DTO.GenreForBook;
import com.friendbook.bookservice.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT new com.friendbook.bookservice.DTO.GenreForBook(g.id, g.name)" +
            " FROM Genre g")
    List<GenreForBook> getAll();
}
