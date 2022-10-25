package com.example.demo.controller;

import com.example.demo.model.dto.ReportTableDto;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.repository.ReportTableQueryRepository;
import com.example.demo.repository.ReportTableRepository;
import com.example.demo.service.ReportTableQueryService;
import com.example.demo.service.ReportTableService;
import com.example.demo.util.TestUtil;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Disabled;
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

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static com.example.demo.util.TestUtil.readFileAsString;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportTableControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ReportTableService reportTableService;
    @Autowired
    private ReportTableRepository reportTableRepository;
    @Autowired
    private ReportTableQueryService reportTableQueryService;
    @Autowired
    private ReportTableQueryRepository reportTableQueryRepository;

    //region POST /api/table/create-table

    @Test
    void create() {
        final ReportTable reqTable = TestUtil.buildTestTable();

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        assertEquals("Artists[ID:INTEGER;NAME:CHARACTER VARYING;AGE:INTEGER;]", TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWithPrimaryKeyOnly() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setColumnsAmount(1);
        reqTable.setColumnInfos(List.of(
            TestUtil.buildTestTableColumn("id", "INTEGER")
        ));

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        assertEquals("Artists[ID:INTEGER;]", TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWhenPrimaryKeyMissing() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setPrimaryKey("id");
        reqTable.setColumnsAmount(1);
        reqTable.setColumnInfos(List.of(
            TestUtil.buildTestTableColumn("name", "VARCHAR")
        ));

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWhenNoColumns() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setPrimaryKey("");
        reqTable.setColumnsAmount(0);
        reqTable.setColumnInfos(Collections.emptyList());

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWithDuplicateName() {
        final ReportTable reqTable = TestUtil.buildTestTable();

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> firstResponse = restTemplate.exchange(request, ReportTable.class);
        then(firstResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(firstResponse.getBody());
        assertEquals("Artists[ID:INTEGER;NAME:CHARACTER VARYING;AGE:INTEGER;]", TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));

        final ResponseEntity<ReportTable> secondResponse = restTemplate.exchange(request, ReportTable.class);
        then(secondResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(secondResponse.getBody());
        assertEquals("Artists[ID:INTEGER;NAME:CHARACTER VARYING;AGE:INTEGER;]", TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWithDuplicateNameDifferentCases() {
        final ReportTable reqTable = TestUtil.buildTestTable();

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        reqTable.setTableName("Artists");
        final ResponseEntity<ReportTable> firstResponse = restTemplate.exchange(request, ReportTable.class);
        then(firstResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(firstResponse.getBody());
        assertEquals("Artists[ID:INTEGER;NAME:CHARACTER VARYING;AGE:INTEGER;]", TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));

        reqTable.setTableName("ARTISTS");
        final ResponseEntity<ReportTable> secondResponse = restTemplate.exchange(request, ReportTable.class);
        then(secondResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(secondResponse.getBody());
    }

    @Test
    void createWithInvalidTableName() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setTableName("CustomerЛАЛА");

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWithInvalidTableColumnTitle() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setPrimaryKey("id");
        reqTable.setColumnsAmount(2);
        reqTable.setColumnInfos(List.of(
            TestUtil.buildTestTableColumn("id", "int4"),
            TestUtil.buildTestTableColumn("ПОЛЕ", "int4")
        ));

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWithInvalidTableColumnType() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setColumnsAmount(1);
        reqTable.setColumnInfos(List.of(
            TestUtil.buildTestTableColumn("id", "ЦИФРА")
        ));

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void createWithWrongColumnAmount() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setColumnsAmount(999);

        final RequestEntity<ReportTable> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reqTable);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo(reqTable.getTableName(), dataSource));
    }

    @Test
    void checkTestCaseFirstValid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/table/create/createTableCustomerValid.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        assertEquals("Customer[" +
                "CUSTOMERID:INTEGER;FIRSTNAME:CHARACTER VARYING;LASTNAME:CHARACTER VARYING;COMPANY:CHARACTER VARYING;" +
                "ADDRESS:CHARACTER VARYING;CITY:CHARACTER VARYING;COUNTRY:CHARACTER VARYING;POSTALCODE:CHARACTER VARYING;" +
                "PHONE:CHARACTER VARYING;FAX:CHARACTER VARYING;EMAIL:CHARACTER VARYING;SUPPORTREPID:INTEGER;]",
            TestUtil.buildSqlTableInfo("Customer", dataSource));
    }

    @Test
    void checkTestCaseFirstInvalid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/table/create/createTableCustomerInvalid.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo("Customer", dataSource));
    }

    @Test
    void checkTestCaseSecondValid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/table/create/createTableArtistValid.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        assertEquals("Artists[ID:INTEGER;NAME:CHARACTER VARYING;AGE:INTEGER;]",
            TestUtil.buildSqlTableInfo("Artists", dataSource));
    }

    @Test
    void checkTestCaseSecondInvalid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/table/create/createTableArtistInvalid.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo("Artists", dataSource));
    }

    @Test
    void checkTestCaseInvalidJson() throws IOException {
        final String exampleBody = readFileAsString("testdataset/table/create/createTableInvalidJson.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/table/create-table"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<ReportTable> response = restTemplate.exchange(request, ReportTable.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());

        assertThrows(RuntimeException.class, () -> TestUtil.buildSqlTableInfo("Artists", dataSource));
    }

    //endregion POST /api/table/create-table

    //region GET /api/table/get-table-by-name/{name}

    @Test
    void getByName() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table/get-table-by-name/" + reqTable.getTableName()))
            .build();

        final ResponseEntity<ReportTableDto> response = restTemplate.exchange(request, ReportTableDto.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(response.getBody(), ReportTableDto.build(reqTable));
    }

    @Test
    void getByNameNotExist() {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table/get-table-by-name/unknown"))
            .build();

        final ResponseEntity<ReportTableDto> response = restTemplate.exchange(request, ReportTableDto.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(response.getBody());
    }

    @Test
    void getByNameWhenNameTooLarge() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reqTable.setTableName("TABLE012345678901234567890123456789012345678901234567890");
        reportTableService.createTable(reqTable);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table/get-table-by-name/" + reqTable.getTableName()))
            .build();

        final ResponseEntity<ReportTableDto> response = restTemplate.exchange(request, ReportTableDto.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNull(response.getBody());
    }

    @Test
    @Disabled
    void getByNameArtistValid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/table/create/createTableArtistValid.json");
        final ReportTable reqTable = JsonMapper.builder().build().readValue(exampleBody, ReportTable.class);
        reportTableService.createTable(reqTable);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table/get-table-by-name/" + reqTable.getTableName()))
            .build();

        final ResponseEntity<ReportTableDto> response = restTemplate.exchange(request, ReportTableDto.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final String exampleResp = readFileAsString("testdataset/table/getByName/getByArtistNameValid.json");
        then(response.getBody()).isEqualTo(JsonMapper.builder().build().readValue(exampleResp, ReportTableDto.class));
    }

    @Test
    @Disabled
    void getByNameCustomerValid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/table/create/createTableCustomerValid.json");
        final ReportTable reqTable = JsonMapper.builder().build().readValue(exampleBody, ReportTable.class);
        reportTableService.createTable(reqTable);

        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/table/get-table-by-name/" + reqTable.getTableName()))
            .build();

        final ResponseEntity<ReportTableDto> response = restTemplate.exchange(request, ReportTableDto.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final String exampleResp = readFileAsString("testdataset/table/getByName/getByCustomerNameValid.json");
        then(response.getBody()).isEqualTo(JsonMapper.builder().build().readValue(exampleResp, ReportTableDto.class));
    }

    //endregion GET /api/table/get-table-by-name/{name}

    //region DELETE /api/table/drop-table/{name}

    @Test
    void deleteByName() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);
        assertNotNull(reportTableRepository.findById(reqTable.getTableName()));

        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table/drop-table-by-name/" + reqTable.getTableName()))
            .build();

        final ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        assertTrue(reportTableRepository.findById(reqTable.getTableName()).isEmpty());
    }

    @Test
    void deleteByNameWhenNotExist() {
        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table/drop-table-by-name/unknown"))
            .build();

        final ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void deleteByNameWithAllTableQueries() {
        final ReportTable reqTable = TestUtil.buildTestTable();
        reportTableService.createTable(reqTable);
        assertNotNull(reportTableRepository.findById(reqTable.getTableName()));

        final ReportTableQuery firstReportTableQuery = TestUtil.buildTestTableQuery();
        firstReportTableQuery.setQueryId(1);
        firstReportTableQuery.setTableName(reqTable.getTableName());
        reportTableQueryService.createTableQuery(firstReportTableQuery);
        assertNotNull(reportTableQueryRepository.findById(firstReportTableQuery.getQueryId()));

        final ReportTableQuery secondReportTableQuery = TestUtil.buildTestTableQuery();
        secondReportTableQuery.setQueryId(2);
        secondReportTableQuery.setTableName(reqTable.getTableName());
        reportTableQueryService.createTableQuery(secondReportTableQuery);
        assertNotNull(reportTableQueryRepository.findById(secondReportTableQuery.getQueryId()));

        assertEquals(2, reportTableQueryRepository.findAllByTableName(reqTable.getTableName()).size());

        final RequestEntity<Void> request = RequestEntity
            .delete(URI.create("http://localhost:" + port + "/api/table/drop-table-by-name/" + reqTable.getTableName()))
            .build();

        final ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        assertTrue(reportTableRepository.findById(reqTable.getTableName()).isEmpty());
        assertTrue(reportTableQueryRepository.findAllByTableName(reqTable.getTableName()).isEmpty());
    }

    //endregion DELETE /api/table/drop-table/{name}
}
