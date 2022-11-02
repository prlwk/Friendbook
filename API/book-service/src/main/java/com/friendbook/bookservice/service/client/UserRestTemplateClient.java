package com.friendbook.bookservice.service.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public Boolean checkToken(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    "http://user-service/user/check-token",
                    HttpMethod.GET,
                    entity, Object.class);
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (Exception exception) {
            return false;
        }
    }

    public Integer getBookGrade(HttpServletRequest request, Long idBook) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Integer> responseEntity = restTemplate.exchange(
                    "http://user-service/user/grade?idBook={id}",
                    HttpMethod.GET,
                    entity, Integer.class, idBook);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
            return null;
        } catch (HttpClientErrorException.NotFound | IllegalStateException exception) {
            return null;
        }
    }

    public Boolean isSavingBook(HttpServletRequest request, Long idBook) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                    "http://user-service/user/is-saving-book?idBook={id}",
                    HttpMethod.GET,
                    entity, Boolean.class, idBook);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
            return null;
        } catch (HttpClientErrorException.NotFound | IllegalStateException exception) {
            return null;
        }
    }
}
