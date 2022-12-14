package com.friendbook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.DTO.AuthorWithBooks;
import com.friendbook.DTO.Book;
import com.friendbook.model.Author;
import com.friendbook.repository.AuthorRepository;
import com.friendbook.service.client.BookRestTemplateClient;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRestTemplateClient bookRestTemplateClient;

    @Override
    public Author getAuthor(Long id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            return authorOptional.get();
        }
        throw new EntityNotFoundException("Author not found.");
    }

    @Override
    public AuthorWithBooks getAuthorWithBooks(Long authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        AuthorWithBooks authorWithBooks = new AuthorWithBooks();
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            Set<Book> books = bookRestTemplateClient.getBooksWithAuthorId(author.getId());
            authorWithBooks.setBooks(bookRestTemplateClient.getBooksWithAuthorId(author.getId()));
            authorWithBooks.setId(author.getId());
            authorWithBooks.setBiography(author.getBiography());
            authorWithBooks.setName(author.getName());
            authorWithBooks.setPhotoSrc("/author/image?id=" + author.getId());
            authorWithBooks.setYearsLife(author.getYearsLife());
            double rating = 0;
            if (books != null) {
                rating = books.stream().map(Book::getRating).mapToDouble(i -> i).sum();
                rating /= books.size();
            }
            authorWithBooks.setRating(rating);
            return authorWithBooks;
        }
        throw new EntityNotFoundException("Author not found.");
    }

    @Override
    public AuthorForBook getAuthorForBook(Long authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        AuthorForBook authorForBook = new AuthorForBook();
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            authorForBook.setId(author.getId());
            authorForBook.setName(author.getName());
            return authorForBook;
        }
        throw new EntityNotFoundException("Author not found.");
    }

    @Override
    public List<AuthorForBook> getAuthorsByAuthorName(String name) {
        Scanner scanner = new Scanner("");
        List<String> listName = new ArrayList<>();
        if (name != null) {
            scanner = new Scanner(name);
            int i = 0;
            while (scanner.hasNext() && i < 3) {
                listName.add(scanner.next());
                i++;
            }
        }
        scanner.close();
        List<String> list = new ArrayList<>(3);
        for (int i = 0; i < listName.size(); i++) {
            if (i == 0) {
                list.add(listName.get(0));
                list.add(listName.get(0));
                list.add(listName.get(0));
            } else {
                list.set(i, listName.get(i));
            }
        }
        List<AuthorForBook> authorForBookList = authorRepository.getAuthorByName(list.get(0), list.get(1), list.get(2));
        if (authorForBookList != null && !authorForBookList.isEmpty()) {
            return authorForBookList;
        }
        throw new EntityNotFoundException("Author not found.");
    }
}
