package com.friendbook.reviewservice.service.client;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.friendbook.reviewservice.DTO.BookForSearch;
import com.friendbook.reviewservice.DTO.ReviewForAccount;

@Component
public class BookRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public Boolean checkGradeExists(Long idBook, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    "http://book-service/book/grade?idBook=" + idBook,
                    HttpMethod.GET,
                    entity, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound exception) {
            return false;
        }
    }

    public List<ReviewForAccount> getRatedBooks(Long userId, List<Long> listIdBook) {
        try {
            ResponseEntity<List<BookForSearch>> responseEntity = restTemplate.exchange(
                    "http://book-service/book/rated-books?userId=" + userId,
                    HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<BookForSearch>>() {
                    });
            return responseEntity
                    .getBody()
                    .stream()
                    .filter(o -> listIdBook.contains(o.getId()))
                    .map(o -> new ReviewForAccount(o.getId(), o.getName(), o.getAuthors(), o.getGrade()))
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        }
    }
}
