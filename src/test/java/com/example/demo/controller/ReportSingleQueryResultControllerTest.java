package com.example.demo.controller;

import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportSingleQueryResult;
import com.example.demo.repository.ReportSingleQueryRepository;
import com.example.demo.repository.ReportSingleQueryResultRepository;
import com.example.demo.service.ReportSingleQueryResultService;
import com.example.demo.service.ReportSingleQueryService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportSingleQueryResultControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportSingleQueryService reportSingleQueryService;
    @Autowired
    private ReportSingleQueryRepository reportSingleQueryRepository;
    @Autowired
    private ReportSingleQueryResultService reportSingleQueryResultService;
    @Autowired
    private ReportSingleQueryResultRepository reportSingleQueryResultRepository;

    //region POST /api/single-query/add-new-query-result

    @Test
    void createAddSingleQueryResult() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    @Test
    void createAddSingleQueryResultWhenCodeIsNull() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setCode(null);

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void createAddSingleQueryResultWithQuery() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setResultId(10);
        reqResult.setCode(200);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> resultResponse = restTemplate.exchange(resultRequest, Void.class);
        then(resultResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(resultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(reqResult.getResultId()).isPresent());
        assertEquals(reqResult, reportSingleQueryResultRepository.findById(reqResult.getResultId()).get());


        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQueryId(5);
        reqQuery.setQuery("SELECT 1");

        final RequestEntity<ReportSingleQuery> queryRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query?resultId=" + reqResult.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isPresent());

        reqQuery.setResult(reqResult);
        assertEquals(reqQuery, reportSingleQueryRepository.findById(reqQuery.getQueryId()).get());
    }

    @Test
    void addSingleQueryWhenResultIsNotExist() {
        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQueryId(5);
        reqQuery.setQuery("SELECT 1");

        final RequestEntity<ReportSingleQuery> queryRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query?resultId=100"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isEmpty());
    }

    //endregion POST /api/single-query/add-new-query-result

    //region POST /api/single-query/add-modify-result

    @Test
    void createModifySingleQueryResult() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-modify-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    @Test
    void createModifySingleQueryResultWhenCodeIsNull() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setCode(null);

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-modify-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void createModifySingleQueryResultWithQuery() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setResultId(10);
        reqResult.setCode(200);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-modify-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> resultResponse = restTemplate.exchange(resultRequest, Void.class);
        then(resultResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(resultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(reqResult.getResultId()).isPresent());
        assertEquals(reqResult, reportSingleQueryResultRepository.findById(reqResult.getResultId()).get());


        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQueryId(5);
        reqQuery.setQuery("SELECT 1");

        final RequestEntity<ReportSingleQuery> queryRequest = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/single-query/modify-single-query?resultId=" + reqResult.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isPresent());

        reqQuery.setResult(reqResult);
        assertEquals(reqQuery, reportSingleQueryRepository.findById(reqQuery.getQueryId()).get());
    }

    @Test
    void modifySingleQueryWhenResultIsNotExist() {
        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQueryId(5);
        reqQuery.setQuery("SELECT 1");

        final RequestEntity<ReportSingleQuery> queryRequest = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/single-query/modify-single-query?resultId=100"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isPresent());
    }

    @Test
    void updateQueryWithNewResult() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setResultId(1);
        reqResult.setCode(200);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> resultResponse = restTemplate.exchange(resultRequest, Void.class);
        then(resultResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(resultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(reqResult.getResultId()).isPresent());
        assertEquals(reqResult, reportSingleQueryResultRepository.findById(reqResult.getResultId()).get());


        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQueryId(1);
        reqQuery.setQuery("SELECT 1");

        final RequestEntity<ReportSingleQuery> queryRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query?resultId=" + reqResult.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isPresent());
        reqQuery.setResult(reqResult);
        assertEquals(reqQuery, reportSingleQueryRepository.findById(reqQuery.getQueryId()).get());


        final ReportSingleQueryResult secondResult = TestUtil.buildTestSingleQueryResult();
        secondResult.setResultId(2);
        secondResult.setCode(404);

        final RequestEntity<ReportSingleQueryResult> secondResultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(secondResult);

        final ResponseEntity<Void> secondResultResponse = restTemplate.exchange(secondResultRequest, Void.class);
        then(secondResultResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(secondResultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(secondResult.getResultId()).isPresent());
        assertEquals(secondResult, reportSingleQueryResultRepository.findById(secondResult.getResultId()).get());


        final RequestEntity<ReportSingleQuery> updateQueryResultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query?resultId=" + secondResult.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> updateQueryResultResponse = restTemplate.exchange(updateQueryResultRequest, Void.class);
        then(updateQueryResultResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(updateQueryResultResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isPresent());

        reqQuery.setResult(secondResult);
        assertEquals(reqQuery, reportSingleQueryRepository.findById(reqQuery.getQueryId()).get());
    }

    //endregion POST /api/single-query/add-modify-result

    //region POST /api/single-query/add-delete-result

    @Test
    void createDeleteSingleQueryResult() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-delete-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    @Test
    void createDeleteSingleQueryResultWhenCodeIsNull() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setCode(null);

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-delete-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void createDeleteSingleQueryResultWithQuery() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setResultId(10);
        reqResult.setCode(200);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-delete-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> resultResponse = restTemplate.exchange(resultRequest, Void.class);
        then(resultResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(resultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(reqResult.getResultId()).isPresent());
        assertEquals(reqResult, reportSingleQueryResultRepository.findById(reqResult.getResultId()).get());


        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQueryId(5);
        reqQuery.setQuery("SELECT 1");

        final RequestEntity<Void> queryRequest = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/single-query/delete-single-query-by-id/" + reqQuery.getQueryId() + "?resultId=" + reqResult.getResultId()))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isPresent());

        reqQuery.setResult(reqResult);
        assertEquals(reqQuery, reportSingleQueryRepository.findById(reqQuery.getQueryId()).get());
    }

    @Test
    void deleteSingleQueryWhenResultIsNotExist() {
        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQueryId(5);
        reqQuery.setQuery("SELECT 1");

        final RequestEntity<Void> queryRequest = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/single-query/delete-single-query-by-id/" + reqQuery.getQueryId() + "?resultId=100"))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(reqQuery.getQueryId()).isPresent());
    }

    //endregion POST /api/single-query/add-delete-result

    //region POST /api/single-query/add-execute-result

    @Test
    void createExecuteSingleQueryResult() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-execute-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    @Test
    void createExecuteSingleQueryResultWhenCodeIsNull() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setCode(null);

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-execute-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void createExecuteSingleQueryResultWithQuery() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setResultId(10);
        reqResult.setCode(200);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-execute-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> resultResponse = restTemplate.exchange(resultRequest, Void.class);
        then(resultResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(resultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(reqResult.getResultId()).isPresent());
        assertEquals(reqResult, reportSingleQueryResultRepository.findById(reqResult.getResultId()).get());


        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final RequestEntity<Void> queryRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + storedQuery.getQueryId() + "?resultId=" + reqResult.getResultId()))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(storedQuery.getQueryId()).isPresent());

        storedQuery.setResult(reqResult);
        assertEquals(storedQuery, reportSingleQueryRepository.findById(storedQuery.getQueryId()).get());
    }

    @Test
    void executeSingleQueryWhenResultIsNotExist() {
        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final RequestEntity<Void> queryRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + storedQuery.getQueryId() + "?resultId=100"))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(storedQuery.getQueryId()).isPresent());
    }

    //endregion POST /api/single-query/add-execute-result

    //region POST /api/single-query/add-get-single-query-by-id-result

    @Test
    void createGetSingleQueryResult() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-get-single-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    @Test
    void createGetSingleQueryResultWhenCodeIsNull() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setCode(null);

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-get-single-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void createGetSingleQueryResultWithQuery() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setResultId(10);
        reqResult.setCode(200);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-get-single-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> resultResponse = restTemplate.exchange(resultRequest, Void.class);
        then(resultResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(resultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(reqResult.getResultId()).isPresent());
        assertEquals(reqResult, reportSingleQueryResultRepository.findById(reqResult.getResultId()).get());


        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final RequestEntity<Void> queryRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-single-query-by-id/" + storedQuery.getQueryId() + "?resultId=" + reqResult.getResultId()))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(storedQuery.getQueryId()).isPresent());

        storedQuery.setResult(reqResult);
        assertEquals(storedQuery, reportSingleQueryRepository.findById(storedQuery.getQueryId()).get());
    }

    @Test
    void getSingleQueryWhenResultIsNotExist() {
        final ReportSingleQuery storedQuery = TestUtil.buildTestSingleQuery();
        storedQuery.setQueryId(5);
        storedQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(storedQuery);


        final RequestEntity<Void> queryRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-single-query-by-id/" + storedQuery.getQueryId() + "?resultId=100"))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(storedQuery.getQueryId()).isPresent());
    }

    //endregion POST /api/single-query/add-get-single-query-by-id-result

    //region POST /api/single-query/add-get-all-single-queries-result

    @Test
    void createGetAllSingleQueriesResult() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-get-all-single-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    @Test
    void createGetAllSingleQueriesResultWhenCodeIsNull() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setCode(null);

        final RequestEntity<ReportSingleQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-get-all-single-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    @Test
    void createGetAllSingleQueriesResultWithQuery() {
        final ReportSingleQueryResult reqResult = TestUtil.buildTestSingleQueryResult();
        reqResult.setResultId(10);
        reqResult.setCode(200);

        final RequestEntity<ReportSingleQueryResult> resultRequest = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-get-all-single-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqResult);

        final ResponseEntity<Void> resultResponse = restTemplate.exchange(resultRequest, Void.class);
        then(resultResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(resultResponse.getBody());
        assertTrue(reportSingleQueryResultRepository.findById(reqResult.getResultId()).isPresent());
        assertEquals(reqResult, reportSingleQueryResultRepository.findById(reqResult.getResultId()).get());


        final ReportSingleQuery firstStoredQuery = TestUtil.buildTestSingleQuery();
        firstStoredQuery.setQueryId(5);
        firstStoredQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(firstStoredQuery);

        final ReportSingleQuery secondStoredQuery = TestUtil.buildTestSingleQuery();
        secondStoredQuery.setQueryId(6);
        secondStoredQuery.setQuery("SELECT 2");
        reportSingleQueryService.createSingleQuery(secondStoredQuery);


        final RequestEntity<Void> queryRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-all-single-queries?resultId=" + reqResult.getResultId()))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(secondStoredQuery.getQueryId()).isPresent());

        firstStoredQuery.setResult(reqResult);
        assertEquals(firstStoredQuery, reportSingleQueryRepository.findById(firstStoredQuery.getQueryId()).get());
        secondStoredQuery.setResult(reqResult);
        assertEquals(secondStoredQuery, reportSingleQueryRepository.findById(secondStoredQuery.getQueryId()).get());
    }

    @Test
    void getAllSingleQueriesWhenResultIsNotExist() {
        final ReportSingleQuery firstStoredQuery = TestUtil.buildTestSingleQuery();
        firstStoredQuery.setQueryId(5);
        firstStoredQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(firstStoredQuery);

        final ReportSingleQuery secondStoredQuery = TestUtil.buildTestSingleQuery();
        secondStoredQuery.setQueryId(6);
        secondStoredQuery.setQuery("SELECT 2");
        reportSingleQueryService.createSingleQuery(secondStoredQuery);


        final RequestEntity<Void> queryRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-all-single-queries?resultId=100"))
            .build();

        final ResponseEntity<Void> queryResponse = restTemplate.exchange(queryRequest, Void.class);
        then(queryResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(queryResponse.getBody());
        assertTrue(reportSingleQueryRepository.findById(secondStoredQuery.getQueryId()).isPresent());
    }

    //endregion POST /api/single-query/add-get-all-single-queries-result
}
