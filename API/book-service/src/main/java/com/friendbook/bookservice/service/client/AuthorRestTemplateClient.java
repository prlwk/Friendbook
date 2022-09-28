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

    public List<AuthorForBook> getAuthorsByName(String name) {
        try {
            ResponseEntity<List> restExchange =
                    restTemplate.exchange(
                            "http://author-service/author/by-name/{name}",
                            HttpMethod.GET,
                            null, List.class, name);
            ObjectMapper mapper = new ObjectMapper();
            List<AuthorForBook> list = new ArrayList<>();
            for (int i = 0; i < restExchange.getBody().size(); i++) {
                list.add(mapper.convertValue(restExchange.getBody().get(i), AuthorForBook.class));
            }
            return list;
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        }
    }
}