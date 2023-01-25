package com.friendbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a " +
            "FROM Author a WHERE LOWER (a.name) LIKE CONCAT('%',LOWER(:name1),'%') " +
            "and LOWER (a.name) LIKE CONCAT('%',LOWER(:name2),'%')" +
            "and LOWER (a.name) LIKE CONCAT('%',LOWER(:name3),'%')")
    List<Author> getAuthorByName(String name1, String name2, String name3);
}
