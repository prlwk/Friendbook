package com.friendbook.reviewservice.service.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public Long getUserId(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity;
        responseEntity = restTemplate.exchange(
                "http://user-service/user/get-user-id",
                HttpMethod.GET,
                entity, Object.class);
        return ((Number) responseEntity.getBody()).longValue();
    }
}
