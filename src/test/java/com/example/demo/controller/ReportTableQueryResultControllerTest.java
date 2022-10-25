package com.example.demo.controller;

import com.example.demo.model.entity.ReportTableQueryResult;
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
public class ReportTableQueryResultControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    //region POST /api/table-query/add-new-query-to-table-result

    @Test
    void createAddTableQueryResult() {
        final ReportTableQueryResult reqResult = TestUtil.buildTestTableQueryResult();
        reqResult.setCode(201);

        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/add-new-query-to-table-result

    //region POST /api/table-query/modify-query-in-table-result

    @Test
    void createModifyTableQueryResult() {
        final ReportTableQueryResult reqResult = TestUtil.buildTestTableQueryResult();
        reqResult.setCode(201);

        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/modify-query-in-table-result

    //region POST /api/table-query/delete-table-query-by-id-result

    @Test
    void createDeleteTableQueryResult() {
        final ReportTableQueryResult reqResult = TestUtil.buildTestTableQueryResult();
        reqResult.setCode(201);

        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/delete-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/delete-table-query-by-id-result

    //region POST /api/table-query/execute-table-query-by-id-result

    @Test
    void createExecuteTableQueryResult() {
        final ReportTableQueryResult reqResult = TestUtil.buildTestTableQueryResult();

        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/execute-table-query-by-id-result

    //region POST /api/table-query/get-all-queries-by-table-name-result

    @Test
    void createGetAllTableQueriesByTableNameResult() {
        final ReportTableQueryResult reqResult = TestUtil.buildTestTableQueryResult();

        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/get-all-queries-by-table-name-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/get-all-queries-by-table-name-result

    //region POST /api/table-query/get-table-query-by-id-result

    @Test
    void createGetTableQueryResult() {
        final ReportTableQueryResult reqResult = TestUtil.buildTestTableQueryResult();

        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/get-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/get-table-query-by-id-result

    //region POST /api/table-query/get-all-table-queries-result

    @Test
    void createGetAllTableQueriesResult() {
        final ReportTableQueryResult reqResult = TestUtil.buildTestTableQueryResult();

        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/get-all-table-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/get-all-table-queries-result
}
