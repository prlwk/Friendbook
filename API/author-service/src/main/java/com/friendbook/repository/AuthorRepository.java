package com.friendbook.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook.DTO.AuthorForSearch;
import com.friendbook.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a " +
            "FROM Author a WHERE LOWER (a.name) LIKE CONCAT('%',LOWER(:name1),'%') " +
            "and LOWER (a.name) LIKE CONCAT('%',LOWER(:name2),'%')" +
            "and LOWER (a.name) LIKE CONCAT('%',LOWER(:name3),'%')")
    List<Author> getAuthorsByName(String name1, String name2, String name3);

    @Query(value = "SELECT new com.friendbook.DTO.AuthorForSearch(a) " +
            "FROM Author a " +
            "WHERE a.id in :listId ",
            countQuery = "SELECT count(a) " +
                    "FROM Author a " +
                    "WHERE a.id in :listId")
    Page<AuthorForSearch> search(@NotNull List<Long> listId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Author a SET a.countRequests = a.countRequests + 1 WHERE a.id = :id")
    void updateCountRequestsByAuthorId(Long id);
}
