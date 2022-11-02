package com.friendbook.service.client;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.friendbook.DTO.Book;

@Component
public class BookRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public Set<Book> getBooksWithAuthorId(Long authorId) {
        try {
            ParameterizedTypeReference<Set<Book>> typeReference =
                    new ParameterizedTypeReference<Set<Book>>() {
                    };
            ResponseEntity<Set<Book>> restExchange =
                    restTemplate.exchange(
                            "http://book-service/book/by-author-id-for-author-page/{authorId}",
                            HttpMethod.GET,
                            null, typeReference, authorId);
            return restExchange.getBody();
        } catch (HttpClientErrorException.NotFound | IllegalStateException exception) {
            return null;
        }
    }
}