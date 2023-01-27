package com.friendbook.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.friendbook.DTO.AuthorForBook;
import com.friendbook.DTO.AuthorForSearch;
import com.friendbook.DTO.AuthorWithBooks;
import com.friendbook.DTO.Book;
import com.friendbook.model.Author;
import com.friendbook.repository.AuthorRepository;
import com.friendbook.service.client.BookRestTemplateClient;
import com.friendbook.utils.Sort;

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
            authorRepository.updateCountRequestsByAuthorId(authorId);
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
    public List<AuthorForSearch> getAuthorsByAuthorName(String name) {
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
        List<Author> authorList = authorRepository.getAuthorsByName(list.get(0), list.get(1), list.get(2));
        List<AuthorForSearch> authorForSearchList = convertAuthorListToAuthorForSearchList(authorList);
        if (authorForSearchList != null) {
            return authorForSearchList;
        }
        throw new EntityNotFoundException("Authors not found.");
    }

    @Override
    public List<AuthorForSearch> getAllAuthorsForSearch() {
        List<Author> authorList = authorRepository.findAll();
        List<AuthorForSearch> authorForSearchList = convertAuthorListToAuthorForSearchList(authorList);
        if (authorForSearchList != null) {
            return authorForSearchList;
        }
        throw new EntityNotFoundException("Authors not found.");
    }

    public List<AuthorForSearch> convertAuthorListToAuthorForSearchList(List<Author> authorList) {
        if (authorList != null && !authorList.isEmpty()) {
            List<AuthorForSearch> authorForSearchList = new ArrayList<>();
            for (Author author : authorList) {
                AuthorForSearch authorForSearch = new AuthorForSearch();
                Set<Book> books = bookRestTemplateClient.getBooksWithAuthorId(author.getId());
                authorForSearch.setId(author.getId());
                authorForSearch.setName(author.getName());
                authorForSearch.setPhotoSrc("/author/image?id=" + author.getId());
                Double rating = null;
                if (books != null) {
                    if (books.isEmpty()) {
                        rating = 0.0;
                    } else {
                        rating = books.stream().map(Book::getRating).mapToDouble(i -> i).sum();
                        rating /= books.size();
                    }
                }
                authorForSearch.setRating(rating);
                authorForSearchList.add(authorForSearch);
            }
            return authorForSearchList;
        }
        return null;
    }

    @Override
    public Page<AuthorForSearch> search(int numberPage, int sizePage, Sort sort, String word, int startRating,
                                        int finishRating) {
        Page<AuthorForSearch> page;
        List<AuthorForSearch> authorForSearchList = new ArrayList<>();
        List<AuthorForSearch> authorForSearchListWithNullRating = new ArrayList<>();
        List<AuthorForSearch> list;
        if (word != null) {
            list = getAuthorsByAuthorName(word);
        } else {
            list = getAllAuthorsForSearch();
        }

        for (AuthorForSearch x : list) {
            Double rating = null;
            Set<Book> books = bookRestTemplateClient.getBooksWithAuthorId(x.getId());
            if (books != null) {
                if (books.isEmpty()) {
                    rating = 0.0;
                } else {
                    rating = books.stream().map(Book::getRating).mapToDouble(i -> i).sum();
                    rating /= books.size();
                }
                if (rating >= startRating && rating <= finishRating) {
                    authorForSearchList.add(new AuthorForSearch(x.getId(), x.getName(), x.getPhotoSrc(),
                            rating, x.getYearsLife()));
                }
            } else {
                authorForSearchListWithNullRating.add(new AuthorForSearch(x.getId(), x.getName(), x.getPhotoSrc(),
                        rating, x.getYearsLife()));
            }
        }
        if (sort == Sort.Rating) {
            authorForSearchList.sort(Comparator.comparing(AuthorForSearch::getRating).reversed());
        } else {
            authorForSearchList.sort(Comparator.comparingLong(x ->
                    authorRepository.findById(((AuthorForSearch) x).getId())
                            .get()
                            .getCountRequests()).reversed());
        }
        authorForSearchList.addAll(authorForSearchListWithNullRating);
        List<Long> listIdAuthor = new ArrayList<>();
        for (AuthorForSearch author : authorForSearchList) {
            listIdAuthor.add(author.getId());
        }
        if (sort == Sort.Popularity) {
            page = authorRepository.search(listIdAuthor, PageRequest.of(numberPage, sizePage,
                    org.springframework.data.domain.Sort.by("countRequests").descending()));
        } else if (sort == Sort.Rating) {
            page = authorRepository.search(listIdAuthor,
                    PageRequest.of(numberPage, sizePage, JpaSort.unsafe("FIELD(a.id, :listId)")));
        } else {
            page = authorRepository.search(listIdAuthor, PageRequest.of(numberPage, sizePage));
        }
        List<AuthorForSearch> result = page.get().toList();
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setRating(authorForSearchList.get(i).getRating());
        }
        return page;
    }
}
