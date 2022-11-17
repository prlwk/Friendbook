package com.friendbook.userservice.service.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ReviewRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public List<Object> getReviews(Long userId) {
        try {
            ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(
                    "http://review-service/review/reviews-for-user?userId=" + userId,
                    HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<Object>>() {
                    });
            return responseEntity.getBody();
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        }
    }
}
