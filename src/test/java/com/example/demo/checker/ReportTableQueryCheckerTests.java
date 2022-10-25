package com.example.demo.checker;

import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.model.entity.ReportTableQueryResult;
import com.example.demo.service.ReportTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportTableQueryCheckerTests {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportTableService reportTableService;

    @Test
    void tableQueryTestCase() {
        createNormally();

        updateNormally();

        deleteNormally();

        executeNormally();

        getByTableNormally();

        getNormally();

        getAllNormally();
    }

    //region CREATE

    void createNormally() {
        final ReportTableQueryResult firstResult = new ReportTableQueryResult();
        firstResult.setResultId(1);
        firstResult.setCode(201);
        assertEquals(202, createTableQueryResult(firstResult));

        final ReportTable firstTable = new ReportTable();
        firstTable.setTableName("test");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(2);
        firstTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        reportTableService.createTable(firstTable);

        final ReportTableQuery firstQuery = new ReportTableQuery();
        firstQuery.setQueryId(1);
        firstQuery.setQuery("SELECT * FROM test LIMIT 666");
        firstQuery.setTableName("test");
        assertEquals(firstResult.getCode(), addTableQuery(firstQuery));
    }

    //endregion CREATE

    //region UPDATE

    void updateNormally() {
        final ReportTableQuery firstQuery = new ReportTableQuery();
        firstQuery.setQueryId(2);
        firstQuery.setQuery("SELECT * FROM test LIMIT 666");
        firstQuery.setTableName("test");
        addTableQuery(firstQuery);

        final ReportTableQueryResult firstResult = new ReportTableQueryResult();
        firstResult.setResultId(2);
        firstResult.setCode(200);
        assertEquals(202, createTableQueryResult(firstResult));

        final ReportTableQuery secondQuery = new ReportTableQuery();
        secondQuery.setQueryId(2);
        secondQuery.setQuery("SELECT * FROM test ORDER BY id DESC");
        secondQuery.setTableName("test");
        assertEquals(firstResult.getCode(), updateTableQuery(secondQuery));
    }

    //endregion UPDATE

    //region DELETE

    void deleteNormally() {
        final ReportTableQuery firstQuery = new ReportTableQuery();
        firstQuery.setQueryId(3);
        firstQuery.setQuery("SELECT * FROM test LIMIT 666");
        firstQuery.setTableName("test");
        addTableQuery(firstQuery);

        final ReportTableQueryResult firstResult = new ReportTableQueryResult();
        firstResult.setResultId(3);
        firstResult.setCode(202);
        assertEquals(202, createTableQueryResult(firstResult));

        assertEquals(firstResult.getCode(), deleteTableQuery(firstQuery));
    }

    //endregion DELETE

    //region EXECUTE

    void executeNormally() {
        final ReportTableQuery firstQuery = new ReportTableQuery();
        firstQuery.setQueryId(4);
        firstQuery.setQuery("SELECT * FROM test LIMIT 666");
        firstQuery.setTableName("test");
        addTableQuery(firstQuery);

        final ReportTableQueryResult firstResult = new ReportTableQueryResult();
        firstResult.setResultId(4);
        firstResult.setCode(201);
        assertEquals(202, createTableQueryResult(firstResult));

        assertEquals(firstResult.getCode(), executeTableQuery(firstQuery));
    }

    //endregion EXECUTE

    //region GET BY TABLE

    void getByTableNormally() {
        final ReportTableQueryResult firstResult = new ReportTableQueryResult();
        firstResult.setResultId(5);
        firstResult.setCode(200);
        assertEquals(202, createTableQueryResult(firstResult));

        final ReportTableQuery firstQuery = new ReportTableQuery();
        firstQuery.setQueryId(5);
        firstQuery.setQuery("SELECT * FROM test LIMIT 666");
        firstQuery.setTableName("test");
        addTableQuery(firstQuery);

        assertEquals(firstResult.getCode(), getTableQueriesByTableName(firstQuery.getTableName()));
    }

    //endregion GET BY TABLE

    //region GET

    void getNormally() {
        final ReportTableQueryResult firstResult = new ReportTableQueryResult();
        firstResult.setResultId(6);
        firstResult.setCode(200);
        assertEquals(202, createTableQueryResult(firstResult));

        final ReportTableQuery firstQuery = new ReportTableQuery();
        firstQuery.setQueryId(6);
        firstQuery.setQuery("SELECT * FROM test LIMIT 666");
        firstQuery.setTableName("test");
        addTableQuery(firstQuery);

        assertEquals(firstResult.getCode(), getTableQuery(firstQuery));
    }

    //endregion GET

    //region ALL

    void getAllNormally() {
        final ReportTableQueryResult firstResult = new ReportTableQueryResult();
        firstResult.setResultId(7);
        firstResult.setCode(200);
        assertEquals(202, createTableQueryResult(firstResult));

        final ReportTableQuery firstQuery = new ReportTableQuery();
        firstQuery.setQueryId(7);
        firstQuery.setQuery("SELECT * FROM test LIMIT 666");
        firstQuery.setTableName("test");
        addTableQuery(firstQuery);

        assertEquals(firstResult.getCode(), getAllTableQueries());
    }

    //endregion ALL

    //

    private int createTableQueryResult(final ReportTableQueryResult result) {
        final RequestEntity<ReportTableQueryResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int addTableQuery(final ReportTableQuery query) {
        final RequestEntity<ReportTableQuery> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table-query/add-new-query-to-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(query);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int updateTableQuery(final ReportTableQuery query) {
        final RequestEntity<ReportTableQuery> request = RequestEntity
            .put(URI.create("http://localhost:" + port + "/api/table-query/modify-query-in-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(query);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int deleteTableQuery(final ReportTableQuery query) {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table-query/delete-table-query-by-id/" + query.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int executeTableQuery(final ReportTableQuery query) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/execute-table-query-by-id/" + query.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getTableQueriesByTableName(final String tableName) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-all-queries-by-table-name/" + tableName))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getTableQuery(final ReportTableQuery query) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-table-query-by-id/" + query.getQueryId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getAllTableQueries() {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table-query/get-all-table-queries"))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }
}
