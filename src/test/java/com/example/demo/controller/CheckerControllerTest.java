package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@Disabled
class CheckerControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void start() {
        final RequestEntity<Void> firstRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/start"))
            .build();

        final ResponseEntity<Void> firstResponse = restTemplate.exchange(firstRequest, Void.class);
        then(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(firstResponse.getBody());
    }

    @Test
    void doubleStart() {
        final RequestEntity<Void> firstRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/start"))
            .build();

        final ResponseEntity<Void> firstResponse = restTemplate.exchange(firstRequest, Void.class);
        then(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(firstResponse.getBody());

        final RequestEntity<Void> secondRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/start"))
            .build();

        final ResponseEntity<Void> secondResponse = restTemplate.exchange(secondRequest, Void.class);
        then(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(secondResponse.getBody());
    }
}
