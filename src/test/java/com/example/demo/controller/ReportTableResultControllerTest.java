package com.example.demo.controller;

import com.example.demo.model.entity.ReportTableResult;
import com.example.demo.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportTableResultControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    //region POST /api/table/add-create-table-result

    @Test
    void createCreateTableResult() {
        final ReportTableResult reqResult = TestUtil.buildTestTableResult();
        reqResult.setCode(201);

        final RequestEntity<ReportTableResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table/add-create-table-result

    //region POST /api/table/get-table-by-name-result

    @Test
    void createGetTableResult() {
        final ReportTableResult reqResult = TestUtil.buildTestTableResult();

        final RequestEntity<ReportTableResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/add-get-table-by-name-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table/get-table-by-name-result

    //region POST /api/table/drop-table-result

    @Test
    void createDropTableResult() {
        final ReportTableResult reqResult = TestUtil.buildTestTableResult();
        reqResult.setCode(201);

        final RequestEntity<ReportTableResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/add-drop-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table/drop-table-result
}
