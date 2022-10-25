package com.example.demo.controller;

import com.example.demo.model.dto.ReportTableQueryDto;
import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.repository.ReportTableQueryRepository;
import com.example.demo.service.ReportSingleQueryService;
import com.example.demo.service.ReportTableQueryService;
import com.example.demo.service.ReportTableService;
import com.example.demo.util.TestUtil;
import org.junit.jupiter.api.Disabled;
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

import javax.sql.DataSource;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportTableQueryControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ReportTableService reportTableService;
    @Autowired
    private ReportTableQueryService reportTableQueryService;
    @Autowired
    private ReportTableQueryRepository reportTableQueryRepository;
    @Autowired
    private ReportSingleQueryService reportSingleQueryService;

    //region POST /api/table-query/add-new-query-to-table

    @Test
    void create() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    @Test
    void createWhenTableNotExist() {
        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void createWhenTableNameTooLarge() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();
        reqTableQuery.setTableName("QUERY12345678901234567890123456789012345678901234567890");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void createWhenQueryTooLarge() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reportTableQuery = TestUtil.buildTestTableQuery();
        reportTableQuery.setQuery("QUERY123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    //endregion POST /api/table-query/add-new-query-to-table

    //region PUT /api/table-query/modify-query-in-table

    @Test
    void update() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();
        reportTableQueryService.createTableQuery(reqTableQuery);

        final ReportTableQuery updatedTableQuery = TestUtil.buildTestTableQuery();
        updatedTableQuery.setQuery("DROP TABLE tests;");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(response.getBody());

        final ReportTableQuery savedQuery = reportTableQueryRepository.findById(reqTableQuery.getQueryId()).get();
        assertEquals(reqTableQuery.getQueryId(), savedQuery.getQueryId());
        assertEquals(updatedTableQuery, savedQuery);
    }

    @Test
    void updateTableName() {
        final ReportTable originReportTable = TestUtil.buildTestTable();
        originReportTable.setTableName("origin");
        reportTableService.createTable(originReportTable);

        final ReportTable newReportTable = TestUtil.buildTestTable();
        newReportTable.setTableName("new");
        reportTableService.createTable(newReportTable);

        final ReportTableQuery reportTableQuery = TestUtil.buildTestTableQuery();
        reportTableQuery.setTableName("origin");
        reportTableQueryService.createTableQuery(reportTableQuery);

        final ReportTableQuery newReportTableQuery = TestUtil.buildTestTableQuery();
        newReportTableQuery.setTableName("new");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(newReportTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(response.getBody());

        final ReportTableQuery resultQuery = reportTableQueryRepository.findById(reportTableQuery.getQueryId()).get();
        assertEquals(reportTableQuery.getQueryId(), resultQuery.getQueryId());
        assertEquals(newReportTableQuery, resultQuery);
    }

    @Test
    void updateWhenNotExists() {
        final ReportTableQuery updatedTableQuery = TestUtil.buildTestTableQuery();
        updatedTableQuery.setQueryId(100);

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void updateWhenTableNotExists() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();
        reportTableQueryService.createTableQuery(reqTableQuery);

        final ReportTableQuery updatedTableQuery = TestUtil.buildTestTableQuery();
        updatedTableQuery.setTableName("unknown");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void updateTooLargeQuer() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();
        reportTableQueryService.createTableQuery(reqTableQuery);

        final ReportTableQuery updatedTableQuery = TestUtil.buildTestTableQuery();
        updatedTableQuery.setTableName("QUERY123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void updateTooLargeTableName() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();
        reportTableQueryService.createTableQuery(reqTableQuery);

        final ReportTableQuery updatedTableQuery = TestUtil.buildTestTableQuery();
        updatedTableQuery.setTableName("TABLE12345678901234567890123456789012345678901234567890");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    //endregion PUT /api/table-query/modify-query-in-table

    //region DELETE api/table-query/delete-table-query-by-id/{id}

    @Test
    void delete() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqQuery = TestUtil.buildTestTableQuery();
        reportTableQueryService.createTableQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table-query/delete-table-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertNull(response.getBody());

        assertTrue(reportTableQueryRepository.findById(reqQuery.getQueryId()).isEmpty());
    }

    @Test
    void deleteNotExist() {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table-query/delete-table-query-by-id/" + 100))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void deleteInvalidQueryId() {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table-query/delete-table-query-by-id/unknown"))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    //endregion DELETE api/table-query/delete-table-query-by-id/{id}

    //region GET /api/table-query/execute-table-query-by-id/{id}

    @Test
    void executeSelectQuery() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqQuery = TestUtil.buildTestTableQuery();
        reportTableQueryService.createTableQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    @Test
    void executeInsertQuery() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqQuery = TestUtil.buildTestTableQuery();
        reqQuery.setQuery("INSERT INTO Artists (id, name, age) VALUES (1, 'John', 23), (2, 'Jane', 21)");
        reportTableQueryService.createTableQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());
    }

    @Test
    void executeUpdateQuery() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqQuery = TestUtil.buildTestTableQuery();
        reqQuery.setQuery("INSERT INTO Artists (id, name, age) VALUES (1, 'John', 23), (2, 'Jane', 21)");
        reportTableQueryService.createTableQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        final ReportTableQuery updateQuery = TestUtil.buildTestTableQuery();
        updateQuery.setQueryId(2);
        updateQuery.setQuery("UPDATE Artists SET age=40 WHERE name='Jane'");
        reportTableQueryService.createTableQuery(updateQuery);

        final RequestEntity<Void> secondRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id/" + updateQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> responseUpdate = restTemplate.exchange(secondRequest, Void.class);
        then(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(responseUpdate.getBody());
    }

    @Test
    void executeDeleteQuery() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqQuery = TestUtil.buildTestTableQuery();
        reqQuery.setQuery("INSERT INTO Artists (id, name, age) VALUES (1, 'John', 23), (2, 'Jane', 21)");
        reportTableQueryService.createTableQuery(reqQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id/" + reqQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        final ReportTableQuery updateQuery = TestUtil.buildTestTableQuery();
        updateQuery.setQueryId(2);
        updateQuery.setQuery("DELETE FROM Artists WHERE name='John'");
        reportTableQueryService.createTableQuery(updateQuery);

        final RequestEntity<Void> secondRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id/" + updateQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> responseUpdate = restTemplate.exchange(secondRequest, Void.class);
        then(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(responseUpdate.getBody());
    }

    //endregion GET /api/table-query/execute-table-query-by-id/{id}

    //region GET /api/table-query/get-all-queries-by-table-name/{name}

    @Test
    void getAllQueriesByTableName() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery firstQuery = TestUtil.buildTestTableQuery();
        firstQuery.setQueryId(1);
        reportTableQueryService.createTableQuery(firstQuery);

        final ReportTableQuery secondQuery = TestUtil.buildTestTableQuery();
        secondQuery.setQueryId(2);
        reportTableQueryService.createTableQuery(secondQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-all-queries-by-table-name/" + firstQuery.getTableName()))
            .build();

        final ResponseEntity<List<ReportTableQueryDto>> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void getAllQueriesByTableNameWhenTableNotExist() {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-all-queries-by-table-name/" + "unknown"))
            .build();

        final ResponseEntity<List<ReportTableQueryDto>> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isNull();
    }

    //region GET /api/table-query/get-table-query-by-id/{id}

    @Test
    void getByQueryId() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final ReportTableQuery firstQuery = TestUtil.buildTestTableQuery();
        firstQuery.setQueryId(1);
        reportTableQueryService.createTableQuery(firstQuery);

        final ReportTableQuery secondQuery = TestUtil.buildTestTableQuery();
        secondQuery.setQueryId(2);
        reportTableQueryService.createTableQuery(secondQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-table-query-by-id/" + secondQuery.getQueryId()))
            .build();

        final ResponseEntity<ReportTableQueryDto> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(ReportTableQueryDto.build(secondQuery));
    }

    @Test
    void getByQueryIdWhenQueryNotExist() {
        final ReportTable reportTable = TestUtil.buildTestTable();
        reportTableService.createTable(reportTable);

        final ReportTableQuery firstQuery = TestUtil.buildTestTableQuery();
        firstQuery.setQueryId(1);
        reportTableQueryService.createTableQuery(firstQuery);

        final ReportTableQuery secondQuery = TestUtil.buildTestTableQuery();
        secondQuery.setQueryId(2);
        reportTableQueryService.createTableQuery(secondQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-table-query-by-id/" + 100))
            .build();

        final ResponseEntity<ReportTableQueryDto> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        then(response.getBody()).isNull();
    }

    //endregion GET /api/table-query/get-table-query-by-id/{id}

    @Test
    void getAll() {
        final ReportTable reportTable = TestUtil.buildTestTable();
        reportTableService.createTable(reportTable);

        final ReportTableQuery firstQuery = TestUtil.buildTestTableQuery();
        firstQuery.setQueryId(1);
        reportTableQueryService.createTableQuery(firstQuery);

        final ReportTableQuery secondQuery = TestUtil.buildTestTableQuery();
        secondQuery.setQueryId(2);
        reportTableQueryService.createTableQuery(secondQuery);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-all-table-queries"))
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
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-all-table-queries"))
            .build();

        final ResponseEntity<List<ReportTableQueryDto>> response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().size()).isEqualTo(0);
    }

    @Test
    @Disabled
    void updateWhenChangeRelatedTable() { // Не успел реализовать! :(
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setTableName("initial");
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();
        reqTableQuery.setTableName("initial");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());


        final ReportSingleQuery changeTableNameQuery = TestUtil.buildTestSingleQuery();
        changeTableNameQuery.setQuery("ALTER TABLE initial RENAME TO updated");
        reportSingleQueryService.createSingleQuery(changeTableNameQuery);

        final RequestEntity<Void> changeTableNameRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + changeTableNameQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> changeTableNameResponse = restTemplate.exchange(changeTableNameRequest, Void.class);
        then(changeTableNameResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(changeTableNameResponse.getBody());


        assertTrue(reportTableService.getByTableName("updated").isPresent());
        assertEquals(1, reportTableQueryService.getAllByTableName("updated").size());
    }

    @Test
    void deleteWhenDropRelatedTable() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setTableName("initial");
        reportTableService.createTable(reqTable);

        final ReportTableQuery reqTableQuery = TestUtil.buildTestTableQuery();
        reqTableQuery.setTableName("initial");

        final RequestEntity<ReportTableQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTableQuery);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());


        assertTrue(reportTableService.getByTableName("initial").isPresent());
        assertEquals(1, reportTableQueryService.getAllByTableName("initial").size());


        final ReportSingleQuery dropTableQuery = TestUtil.buildTestSingleQuery();
        dropTableQuery.setQuery("drop table initial;");
        reportSingleQueryService.createSingleQuery(dropTableQuery);

        final RequestEntity<Void> dropTableRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/single-query/execute-single-query-by-id/" + dropTableQuery.getQueryId()))
            .build();

        final ResponseEntity<Void> dropTableResponse = restTemplate.exchange(dropTableRequest, Void.class);
        then(dropTableResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(dropTableResponse.getBody());


        assertTrue(reportTableService.getByTableName("initial").isEmpty());
        assertEquals(0, reportTableQueryService.getAllByTableName("initial").size());
    }
}
