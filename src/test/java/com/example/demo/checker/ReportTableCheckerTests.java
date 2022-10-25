package com.example.demo.checker;

import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import com.example.demo.model.entity.ReportTableResult;
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
public class ReportTableCheckerTests {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportTableService reportTableService;

    @Test
    void reportTableTestCase() {
        createNormally();

        getNormally();

        deleteNormally();
    }

    //region CREATE

    void createNormally() {
        final ReportTableResult firstResult = new ReportTableResult();
        firstResult.setResultId(1);
        firstResult.setCode(201);
        assertEquals(201, createTableResult(firstResult));

        final ReportTable firstTable = new ReportTable();
        firstTable.setTableName("test");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(2);
        firstTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));

        assertEquals(firstResult.getCode(), createTable(firstTable));
    }

    //endregion CREATE

    //region GET

    void getNormally() {
        final ReportTableResult firstResult = new ReportTableResult();
        firstResult.setResultId(2);
        firstResult.setCode(200);
        assertEquals(201, createTableResult(firstResult));

        final ReportTable firstTable = new ReportTable();
        firstTable.setTableName("test2");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(2);
        firstTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        reportTableService.createTable(firstTable);

        assertEquals(firstResult.getCode(), getTable(firstTable.getTableName()));
    }

    //endregion GET

    //region DELETE

    void deleteNormally() {
        final ReportTableResult firstResult = new ReportTableResult();
        firstResult.setResultId(3);
        firstResult.setCode(201);
        assertEquals(201, createTableResult(firstResult));

        final ReportTable firstTable = new ReportTable();
        firstTable.setTableName("test3");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(2);
        firstTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        reportTableService.createTable(firstTable);

        assertEquals(firstResult.getCode(), deleteTable(firstTable.getTableName()));
    }

    //endregion DELETE

    //

    private int createTableResult(final ReportTableResult result) {
        final RequestEntity<ReportTableResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int createTable(final ReportTable table) {
        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(table);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getTable(final String tableName) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table/get-table-by-name/" + tableName))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int deleteTable(final String tableName) {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table/drop-table/" + tableName))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }
}
