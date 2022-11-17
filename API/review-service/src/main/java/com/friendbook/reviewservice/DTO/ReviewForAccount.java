package com.friendbook.reviewservice.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewForAccount {
    private String text;
    private Long bookId;
    private String bookName;
    private List<AuthorForBook> authorForBookList;
    private long date;
    private int grade;

    public ReviewForAccount(Long bookId, String bookName, List<AuthorForBook> authorForBookList, int grade) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorForBookList = authorForBookList;
        this.grade = grade;
    }
}
