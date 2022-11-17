package com.friendbook.bookservice.service.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ReviewRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public void setReview(String text, int grade, long idBook, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> restExchange = restTemplate.exchange("http://review-service/review/set-review?text=" + text + "&grade=" + grade + "&idBook=" + idBook, HttpMethod.GET,
                entity, Object.class);
    }

    public void deleteReview(Long idBook, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> restExchange = restTemplate.exchange("http://review-service/review/delete-review?idBook=" + idBook, HttpMethod.GET,
                entity, Object.class);
    }
}
