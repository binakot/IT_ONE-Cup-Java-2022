package com.example.demo.controller;

import com.example.demo.model.dto.ReportSingleQueryDto;
import com.example.demo.model.dto.ReportTableQueryDto;
import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.repository.ReportSingleQueryRepository;
import com.example.demo.service.ReportSingleQueryService;
import com.example.demo.service.ReportTableService;
import com.example.demo.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ReportSingleQueryControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportSingleQueryService reportSingleQueryService;
    @Autowired
    private ReportSingleQueryRepository reportSingleQueryRepository;
    @Autowired
    private ReportTableService reportTableService;

    //region POST /api/single-query/add-new-query

    @Test
    void create() {
        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();

        final RequestEntity<ReportSingleQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    @Test
    void createWhenTooLongQuery() {
        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQuery("QUERY123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        final RequestEntity<ReportSingleQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/single-query/add-new-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNull(response.getBody());
    }

    //endregion POST /api/single-query/add-new-query

    //region PUT /api/single-query/modify-single-query

    @Test
    void update() {
        final ReportSingleQuery originQuery = TestUtil.buildTestSingleQuery();
        reportSingleQueryService.createSingleQuery(originQuery);

        final ReportSingleQuery updatedQuery = TestUtil.buildTestSingleQuery();
        updatedQuery.setQueryId(originQuery.getQueryId());
        updatedQuery.setQuery("SELECT 1");

        final RequestEntity<ReportSingleQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/single-query/modify-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(response.getBody());

        final ReportSingleQuery query = reportSingleQueryRepository.findById(originQuery.getQueryId()).get();
        assertEquals(originQuery.getQueryId(), query.getQueryId());
        assertEquals(updatedQuery, query);
    }

    @Test
    void updateNotExists() {
        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();

        final RequestEntity<ReportSingleQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/single-query/modify-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void updateWhenQueryTooLarge() {
        final ReportSingleQuery originQuery = TestUtil.buildTestSingleQuery();
        reportSingleQueryService.createSingleQuery(originQuery);

        final ReportSingleQuery updatedQuery = new ReportSingleQuery();
        updatedQuery.setQueryId(originQuery.getQueryId());
        updatedQuery.setQuery("QUERY123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        final RequestEntity<ReportSingleQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/single-query/modify-query"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    //endregion PUT /api/single-query/modify-single-query

    //region DELETE /api/single-query/delete-single-query-by-id/{id}

    @Test
    void delete() {
        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reportSingleQueryService.createSingleQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/single-query/delete-single-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());
    }

    @Test
    void deleteNotExist() {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/single-query/delete-single-query-by-id/" + 100))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void deleteInvalidId() {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/single-query/delete-single-query-by-id/unknown"))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    //endregion DELETE /api/single-query/delete-single-query-by-id/{id}

    //region GET /api/single-query/execute-single-query-by-id/{id}

    @Test
    void execute() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQuery("SELECT * FROM " + reqTable.getTableName() + " ORDER BY id DESC LIMIT 1");
        reportSingleQueryService.createSingleQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    @Test
    void executeWhenNotExists() {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/100"))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void executeWhenTableNotExists() {
        final ReportSingleQuery reqQuery = TestUtil.buildTestSingleQuery();
        reqQuery.setQuery("SELECT * FROM unknown ORDER BY id DESC LIMIT 1");
        reportSingleQueryService.createSingleQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    //region GET /api/single-query/execute-single-query-by-id/{id}

    //region GET /api/single-query/get-single-query-by-id/{id}

    @Test
    void getByQueryId() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportSingleQuery firstQuery = TestUtil.buildTestSingleQuery();
        firstQuery.setQueryId(1);
        reportSingleQueryService.createSingleQuery(firstQuery);

        final ReportSingleQuery secondQuery = TestUtil.buildTestSingleQuery();
        secondQuery.setQueryId(2);
        reportSingleQueryService.createSingleQuery(secondQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-single-query-by-id/" + secondQuery.getQueryId()))
            .build();

        final ResponseEntity<ReportSingleQueryDto> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(ReportSingleQueryDto.build(secondQuery));
    }

    @Test
    void getByQueryIdWhenQueryNotExist() {
        final ReportTable reportTable = TestUtil.buildTestTable();
        reportTableService.createTable(reportTable);

        final ReportSingleQuery firstQuery = TestUtil.buildTestSingleQuery();
        firstQuery.setQueryId(1);
        reportSingleQueryService.createSingleQuery(firstQuery);

        final ReportSingleQuery secondQuery = TestUtil.buildTestSingleQuery();
        secondQuery.setQueryId(2);
        reportSingleQueryService.createSingleQuery(secondQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-single-query-by-id/" + 100))
            .build();

        final ResponseEntity<ReportSingleQueryDto> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        then(response.getBody()).isNull();
    }

    //endregion GET /api/single-query/get-single-query-by-id/{id}

    @Test
    void getAll() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportSingleQuery firstQuery = TestUtil.buildTestSingleQuery();
        firstQuery.setQueryId(1);
        firstQuery.setQuery("SELECT * FROM " + reqTable.getTableName());
        reportSingleQueryService.createSingleQuery(firstQuery);

        final ReportSingleQuery secondQuery = TestUtil.buildTestSingleQuery();
        secondQuery.setQueryId(2);
        secondQuery.setQuery("SELECT 1");
        reportSingleQueryService.createSingleQuery(secondQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-all-single-queries"))
            .build();

        final ResponseEntity<List<ReportTableQueryDto>> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void getAllWhenEmpty() {
        final ReportTable reportTable = TestUtil.buildTestTable();
        reportTableService.createTable(reportTable);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/get-all-single-queries"))
            .build();

        final ResponseEntity<List<ReportSingleQueryDto>> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }
}
