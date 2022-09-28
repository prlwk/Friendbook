package com.friendbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT new com.friendbook.DTO.AuthorForBook(a.id, a.name)" +
            "FROM Author a WHERE LOWER (a.name) LIKE CONCAT('%',LOWER(:name1),'%') " +
            "and LOWER (a.name) LIKE CONCAT('%',LOWER(:name2),'%')" +
            "and LOWER (a.name) LIKE CONCAT('%',LOWER(:name3),'%')")
    List<AuthorForBook> getAuthorByName(String name1, String name2, String name3);
}
