package com.example.demo.controller;

import com.example.demo.model.dto.ReportCreateDto;
import com.example.demo.model.dto.ReportReadDto;
import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.repository.ReportRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.example.demo.util.TestUtil.buildTestTableColumn;
import static com.example.demo.util.TestUtil.readFileAsString;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ReportControllerTest {

    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportTableService reportTableService;
    @Autowired
    private ReportSingleQueryService reportSingleQueryService;

    @Test
    void create() {
        final ReportTable firstTable = TestUtil.buildTestTable();
        firstTable.setTableName("Artists");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(3);
        firstTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("age", "int4")
        ));
        reportTableService.createTable(firstTable);

        final ReportTable secondTable = TestUtil.buildTestTable();
        secondTable.setTableName("Job");
        secondTable.setPrimaryKey("id");
        secondTable.setColumnsAmount(4);
        secondTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("salary", "int4"),
            buildTestTableColumn("address", "varchar")
        ));
        reportTableService.createTable(secondTable);

        final ReportCreateDto newReport = new ReportCreateDto();
        newReport.setReportId(1);
        newReport.setTableAmount(2);
        newReport.setTables(List.of(
            ReportCreateDto.ReportCreateTableDto.build(firstTable),
            ReportCreateDto.ReportCreateTableDto.build(secondTable)
        ));

        final RequestEntity<ReportCreateDto> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(newReport);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        final Optional<Report> storedReport = reportRepository.findById(newReport.getReportId());
        assertTrue(storedReport.isPresent());
        assertEquals(newReport.getReportId(), storedReport.get().getReportId());
        assertEquals(newReport.getTableAmount(), storedReport.get().getTableAmount());
    }

    @Test
    void checkTestCaseFirstValid() throws IOException {
        final ReportTable firstTable = TestUtil.buildTestTable();
        firstTable.setTableName("Artists");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(3);
        firstTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("age", "int4")
        ));
        reportTableService.createTable(firstTable);

        final String exampleBody = readFileAsString("testdataset/report/firstValidReport.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<Report> response = restTemplate.exchange(request, Report.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        final Optional<Report> storedReport = reportRepository.findById(1);
        assertTrue(storedReport.isPresent());
        assertEquals(1, storedReport.get().getReportId());
        assertEquals(1, storedReport.get().getTableAmount());
    }

    @Test
    void checkTestCaseSecondValid() throws IOException {
        final ReportTable firstTable = TestUtil.buildTestTable();
        firstTable.setTableName("Artists");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(3);
        firstTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("age", "int4")
        ));
        reportTableService.createTable(firstTable);

        final ReportTable secondTable = TestUtil.buildTestTable();
        secondTable.setTableName("Job");
        secondTable.setPrimaryKey("id");
        secondTable.setColumnsAmount(4);
        secondTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("salary", "int4"),
            buildTestTableColumn("address", "varchar")
        ));
        reportTableService.createTable(secondTable);

        final String exampleBody = readFileAsString("testdataset/report/secondValidReport.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<Report> response = restTemplate.exchange(request, Report.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());

        final Optional<Report> storedReport = reportRepository.findById(2);
        assertTrue(storedReport.isPresent());
        assertEquals(2, storedReport.get().getReportId());
        assertEquals(2, storedReport.get().getTableAmount());
    }

    @Test
    void checkTestCaseFirstInvalid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/report/firstInvalidReport.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<Report> response = restTemplate.exchange(request, Report.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void checkTestCaseSecondInvalid() throws IOException {
        final String exampleBody = readFileAsString("testdataset/report/secondInvalidReport.json");

        final RequestEntity<String> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(exampleBody);

        final ResponseEntity<Report> response = restTemplate.exchange(request, Report.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertNull(response.getBody());
    }

    @Test
    void get() {
        final ReportTable firstTable = TestUtil.buildTestTable();
        firstTable.setTableName("Artists");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(3);
        firstTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("age", "int4")
        ));
        reportTableService.createTable(firstTable);

        final ReportTable secondTable = TestUtil.buildTestTable();
        secondTable.setTableName("Job");
        secondTable.setPrimaryKey("id");
        secondTable.setColumnsAmount(4);
        secondTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("salary", "int4"),
            buildTestTableColumn("address", "varchar")
        ));
        reportTableService.createTable(secondTable);


        final ReportSingleQuery firstSingleQuery = TestUtil.buildTestSingleQuery();
        firstSingleQuery.setQueryId(1);
        firstSingleQuery.setQuery("INSERT INTO Artists (id, name, age) " +
            "VALUES (1, 'John', 10), (2, 'Jane', 20), (3, NULL, 30)");
        reportSingleQueryService.createSingleQuery(firstSingleQuery);
        reportSingleQueryService.execute(firstSingleQuery);

        final ReportSingleQuery secondSingleQuery = TestUtil.buildTestSingleQuery();
        secondSingleQuery.setQueryId(2);
        secondSingleQuery.setQuery("INSERT INTO Job (id, name, salary, address) " +
            "VALUES (1, 'John', 100, NULL), (2, 'Jane', 200, NULL)");
        reportSingleQueryService.createSingleQuery(secondSingleQuery);
        reportSingleQueryService.execute(secondSingleQuery);


        final ReportCreateDto report = new ReportCreateDto();
        report.setReportId(1);
        report.setTableAmount(2);
        report.setTables(List.of(
            ReportCreateDto.ReportCreateTableDto.build(firstTable),
            ReportCreateDto.ReportCreateTableDto.build(secondTable)
        ));

        final RequestEntity<ReportCreateDto> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(report);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());


        final RequestEntity<Void> reportRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/report/get-report-by-id/" + report.getReportId()))
            .build();

        final ResponseEntity<ReportReadDto> reportResponse = restTemplate.exchange(reportRequest, ReportReadDto.class);
        then(reportResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final ReportReadDto dto = reportResponse.getBody();
        assertNotNull(dto);
        assertEquals(1, dto.getReportId());
        assertEquals(2, dto.getTableAmount());
        assertEquals(2, dto.getTables().size());
        dto.getTables().forEach(t -> {
            t.getColumns().forEach(c -> {
                System.out.println(t.getTableName() + "." + c.getTitle() + ":" + c.getSize());
            });
        });
    }

    @Test
    void getAfterRemoveColumn() {
        final ReportTable firstTable = TestUtil.buildTestTable();
        firstTable.setTableName("Artists");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(3);
        firstTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("age", "int4")
        ));
        reportTableService.createTable(firstTable);

        final ReportTable secondTable = TestUtil.buildTestTable();
        secondTable.setTableName("Job");
        secondTable.setPrimaryKey("id");
        secondTable.setColumnsAmount(4);
        secondTable.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("salary", "int4"),
            buildTestTableColumn("address", "varchar")
        ));
        reportTableService.createTable(secondTable);


        final ReportSingleQuery firstSingleQuery = TestUtil.buildTestSingleQuery();
        firstSingleQuery.setQueryId(1);
        firstSingleQuery.setQuery("INSERT INTO Artists (id, name, age) " +
            "VALUES (1, 'John', 10), (2, 'Jane', 20), (3, NULL, 30)");
        reportSingleQueryService.createSingleQuery(firstSingleQuery);
        reportSingleQueryService.execute(firstSingleQuery);

        final ReportSingleQuery secondSingleQuery = TestUtil.buildTestSingleQuery();
        secondSingleQuery.setQueryId(2);
        secondSingleQuery.setQuery("INSERT INTO Job (id, name, salary, address) " +
            "VALUES (1, 'John', 100, NULL), (2, 'Jane', 200, NULL)");
        reportSingleQueryService.createSingleQuery(secondSingleQuery);
        reportSingleQueryService.execute(secondSingleQuery);


        final ReportCreateDto report = new ReportCreateDto();
        report.setReportId(1);
        report.setTableAmount(2);
        report.setTables(List.of(
            ReportCreateDto.ReportCreateTableDto.build(firstTable),
            ReportCreateDto.ReportCreateTableDto.build(secondTable)
        ));

        final RequestEntity<ReportCreateDto> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(report);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNull(response.getBody());


        final ReportSingleQuery removeColumnQuery = TestUtil.buildTestSingleQuery();
        removeColumnQuery.setQueryId(3);
        removeColumnQuery.setQuery("ALTER table Job DROP COLUMN salary;");
        reportSingleQueryService.createSingleQuery(removeColumnQuery);
        reportSingleQueryService.execute(removeColumnQuery);


        final RequestEntity<Void> reportRequest = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/report/get-report-by-id/" + report.getReportId()))
            .build();

        final ResponseEntity<ReportReadDto> reportResponse = restTemplate.exchange(reportRequest, ReportReadDto.class);
        then(reportResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final ReportReadDto dto = reportResponse.getBody();
        assertNotNull(dto);
        assertEquals(1, dto.getReportId());
        assertEquals(2, dto.getTableAmount());
        assertEquals(2, dto.getTables().size());
        dto.getTables().forEach(t -> {
            t.getColumns().forEach(c -> {
                System.out.println(t.getTableName() + "." + c.getTitle() + ":" + c.getSize());
            });
        });
    }
}
