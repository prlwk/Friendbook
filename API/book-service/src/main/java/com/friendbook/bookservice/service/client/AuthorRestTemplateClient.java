package com.friendbook.bookservice.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.friendbook.bookservice.DTO.AuthorForBook;

@Component
public class AuthorRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public AuthorForBook getAuthorById(Long authorId) {
        try {
            ResponseEntity<AuthorForBook> restExchange =
                    restTemplate.exchange(
                            "http://author-service/author/for-book/{authorId}",
                            HttpMethod.GET,
                            null, AuthorForBook.class, authorId);
            return restExchange.getBody();
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        }
    }
}