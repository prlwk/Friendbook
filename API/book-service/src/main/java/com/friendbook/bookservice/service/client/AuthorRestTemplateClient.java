package com.friendbook.bookservice.service.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendbook.bookservice.DTO.AuthorForBook;
import com.friendbook.bookservice.DTO.AuthorForSearch;

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
        } catch (HttpClientErrorException.NotFound | IllegalStateException exception) {
            return null;
        }
    }

    public List<AuthorForSearch> getAuthorsByName(String name) {
        try {
            ResponseEntity<List> restExchange =
                    restTemplate.exchange(
                            "http://author-service/author/by-name/{name}",
                            HttpMethod.GET,
                            null, List.class, name);
            ObjectMapper mapper = new ObjectMapper();
            List<AuthorForSearch> list = new ArrayList<>();
            for (int i = 0; i < restExchange.getBody().size(); i++) {
                list.add(mapper.convertValue(restExchange.getBody().get(i), AuthorForSearch.class));
            }
            return list;
        } catch (HttpClientErrorException.NotFound | IllegalStateException exception) {
            return null;
        }
    }
}