package com.friendbook.bookservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.bookservice.DTO.TagForBook;
import com.friendbook.bookservice.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT new com.friendbook.bookservice.DTO.TagForBook(t.id, t.name)" +
            " FROM Tag t")
    List<TagForBook> getAll();
}
