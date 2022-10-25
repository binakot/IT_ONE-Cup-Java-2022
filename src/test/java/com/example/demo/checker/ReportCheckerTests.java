package com.example.demo.checker;

import com.example.demo.model.dto.ReportCreateDto;
import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.ReportResult;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportCheckerTests {

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
    }

    //region CREATE

    void createNormally() {
        final ReportResult firstResult = new ReportResult();
        firstResult.setResultId(1);
        firstResult.setCode(201);
        assertEquals(202, createReportResult(firstResult));

        final ReportTable firstTable = new ReportTable();
        firstTable.setTableName("test");
        firstTable.setPrimaryKey("id");
        firstTable.setColumnsAmount(2);
        firstTable.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        reportTableService.createTable(firstTable);

        final Report firstReport = new Report();
        firstReport.setReportId(1);
        firstReport.setTableAmount(1);
        firstReport.setTables(List.of(firstTable));

        assertEquals(firstResult.getCode(), createReport(firstReport));
    }

    //endregion CREATE

    //region GET

    void getNormally() {
        final ReportResult firstResult = new ReportResult();
        firstResult.setResultId(1);
        firstResult.setCode(201);
        assertEquals(202, createReportResult(firstResult));

        final Report firstReport = new Report();
        firstReport.setReportId(1);

        assertEquals(firstResult.getCode(), getReport(firstReport));
    }

    //endregion GET

    //

    private int createReportResult(final ReportResult result) {
        final RequestEntity<ReportResult> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/add-get-report-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(result);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int createReport(final Report report) {
        ReportCreateDto createDto = new ReportCreateDto();
        createDto.setReportId(report.getReportId());
        createDto.setTableAmount(report.getTableAmount());
        createDto.setTables(report.getTables().stream().map(ReportCreateDto.ReportCreateTableDto::build).collect(Collectors.toList()));

        final RequestEntity<ReportCreateDto> request = RequestEntity
            .post(URI.create("http://localhost:" + port + "/api/report/create-report"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(createDto);

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }

    private int getReport(final Report report) {
        final RequestEntity<Void> request = RequestEntity
            .get(URI.create("http://localhost:" + port + "/api/report/get-report-by-id/" + report.getReportId()))
            .build();

        final ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
        return response.getStatusCode().value();
    }
}
