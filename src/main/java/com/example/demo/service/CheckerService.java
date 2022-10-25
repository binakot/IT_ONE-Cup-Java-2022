package com.example.demo.service;

import com.example.demo.model.dto.ReportBodyListTableQueryResult;
import com.example.demo.model.dto.ReportBodyResult;
import com.example.demo.model.dto.ReportBodySingleQueryResult;
import com.example.demo.model.dto.ReportBodyTableQueryResult;
import com.example.demo.model.dto.ReportBodyTableResult;
import com.example.demo.model.dto.ReportCreateDto;
import com.example.demo.model.dto.ReportReadDto;
import com.example.demo.model.dto.ReportTableDto;
import com.example.demo.model.dto.ReportTableQueryDto;
import com.example.demo.model.entity.ReportResult;
import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportSingleQueryResult;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.model.entity.ReportTableQueryResult;
import com.example.demo.model.entity.ReportTableResult;
import com.example.demo.util.FinalCheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
//@Profile("!test")
public class CheckerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${rs.endpoint:http://localhost:9081}")
    private String hostName;

    public void run(final int targetNumber) {
        FinalCheckUtil.killThemAll(hostName, restTemplate, targetNumber);
    }

    /**
     * uri=/api/table/create-table
     * uri=/api/table/drop-table/Artists
     * uri=/api/table/drop-table/Artists
     * uri=/api/table/create-table
     * uri=/api/table/create-table
     * uri=/api/table/drop-table/Artist
     * uri=/api/table/drop-table/Artist
     * uri=/api/table/create-table
     * uri=/api/table/get-table-by-name/Artist
     * uri=/api/table/create-table
     * uri=/api/table/drop-table/Customer
     * uri=/api/table/drop-table/Customer
     * uri=/api/table/create-table
     * uri=/api/table-query/add-new-query-to-table
     * uri=/api/table-query/add-new-query-to-table
     * uri=/api/table-query/get-all-table-queries
     * uri=/api/table-query/add-new-query-to-table
     * uri=/api/table-query/get-all-table-queries
     * uri=/api/table-query/add-new-query-to-table
     * uri=/api/table-query/add-new-query-to-table
     * uri=/api/table-query/delete-table-query-by-id/5
     * uri=/api/table-query/delete-table-query-by-id/6
     * uri=/api/table-query/delete-table-query-by-id/5
     * uri=/api/table-query/modify-query-in-table
     * uri=/api/single-query/add-new-query
     * uri=/api/single-query/get-all-single-queries
     * uri=/api/single-query/add-new-query
     * uri=/api/single-query/get-all-single-queries
     * uri=/api/single-query/add-new-query
     * uri=/api/single-query/get-all-single-queries
     * uri=/api/single-query/add-new-query
     * uri=/api/single-query/get-all-single-queries
     * uri=/api/single-query/add-new-query
     * uri=/api/single-query/get-all-single-queries
     * uri=/api/single-query/delete-single-query-by-id/5
     * uri=/api/single-query/delete-single-query-by-id/6
     * uri=/api/single-query/delete-single-query-by-id/5
     * uri=/api/report/create-report
     * uri=/api/report/create-report
     * uri=/api/report/create-report
     */
    //@EventListener(ApplicationReadyEvent.class)
    public void runQualificationCheck() {
        try {
            // 30: table tests
            createTableArtists();
            dropTableArtists();
            dropTableArtistsAgain();
            createTableArtistsAgain();
            createTableArtist();
            dropTableArtist();
            dropTableArtistAgain();
            createTableArtistAgain();
            getTableArtistByName();
            createTableCustomer();
            dropTableCustomer();
            createTableCustomerAgain();

            // 20: table query tests
            createTableQueryForAlbumTable();
            createTableQueryForArtistTable();
            getAllTableQueries();
            createTableQueryForCustomerTable();
            getAllTableQueriesAgain();
            createTableQueryForEmployeeTable();
            createTableQueryForGenreTable();
            getTableQueryByIdForGenre();
            executeTableQueryByIdForGenre();
            deleteTableQueryWithQueryId5();
            deleteTableQueryWithQueryId6();
            deleteTableQueryWithQueryId5Again();
            modifyTableQueryWithQueryId4();
            getAllQueriesByTableNameForGenre();

            // 20: single query tests
            createSingleQueryForAlbum();
            getAllSingleQueries();
            createSingleQueryForArtist();
            getAllSingleQueriesAgain();
            createSingleQueryForCustomer();
            modifySingleQueryForCustomer();
            executeSingleQueryForCustomer();
            createSingleQueryForEmployee();
            createSingleQueryForGenre();
            getSingleQueryForGenreById();
            deleteSingleQueryForGenre();
            deleteSingleQueryForQueryId6();
            deleteSingleQueryForQueryId5Again();

            // 30: report tests
            createReportForArtists();
            getReportForArtists();
            createReportForArtistsAndJob();
            createReportForArtistsAndArtist();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    //region TABLE

    private void createTableArtists() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(1);
        reportTableResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable reportTable1 = new ReportTable();
        reportTable1.setTableName("Artists");
        reportTable1.setColumnsAmount(3);
        reportTable1.setPrimaryKey("id");
        reportTable1.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("age", "int4")
        ));
        final RequestEntity<ReportTable> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/create-table?resultId=" + reportTableResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTable1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableArtists: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableArtists");
            }
        }
    }

    private void dropTableArtists() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(2);
        reportTableResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-drop-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table/drop-table/Artists?resultId=" + reportTableResult1.getResultId()))
            .build(); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> dropTableArtists: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: dropTableArtists");
            }
        }
    }

    private void dropTableArtistsAgain() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(3);
        reportTableResult1.setCode(406); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-drop-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table/drop-table/Artists?resultId=" + reportTableResult1.getResultId()))
            .build(); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> dropTableArtistsAgain: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: dropTableArtistsAgain");
            }
        }
    }

    private void createTableArtistsAgain() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(4);
        reportTableResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable reportTable1 = new ReportTable();
        reportTable1.setTableName("Artists");
        reportTable1.setColumnsAmount(3);
        reportTable1.setPrimaryKey("id");
        reportTable1.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("age", "int4")
        ));
        final RequestEntity<ReportTable> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/create-table?resultId=" + reportTableResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTable1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableArtistsAgain: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableArtistsAgain");
            }
        }
    }

    private void createTableArtist() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(5);
        reportTableResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable reportTable1 = new ReportTable();
        reportTable1.setTableName("Artist");
        reportTable1.setColumnsAmount(2);
        reportTable1.setPrimaryKey("artistId");
        reportTable1.setColumnInfos(List.of(
            new ReportTableColumn("artistId", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        final RequestEntity<ReportTable> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/create-table?resultId=" + reportTableResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTable1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableArtist: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableArtist");
            }
        }
    }

    private void dropTableArtist() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(6);
        reportTableResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-drop-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table/drop-table/Artist?resultId=" + reportTableResult1.getResultId()))
            .build(); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> dropTableArtist: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: dropTableArtist");
            }
        }
    }

    private void dropTableArtistAgain() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(7);
        reportTableResult1.setCode(406); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-drop-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table/drop-table/Artist?resultId=" + reportTableResult1.getResultId()))
            .build(); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> dropTableArtistAgain: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: dropTableArtistAgain");
            }
        }
    }

    private void createTableArtistAgain() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(8);
        reportTableResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable reportTable1 = new ReportTable();
        reportTable1.setTableName("Artist");
        reportTable1.setColumnsAmount(2);
        reportTable1.setPrimaryKey("artistId");
        reportTable1.setColumnInfos(List.of(
            new ReportTableColumn("artistId", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        final RequestEntity<ReportTable> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/create-table?resultId=" + reportTableResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTable1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableArtistAgain: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableArtistAgain");
            }
        }
    }

    private void getTableArtistByName() {
        final ReportTable reportTable1 = new ReportTable();
        reportTable1.setTableName("Artist");
        reportTable1.setColumnsAmount(2);
        reportTable1.setPrimaryKey("artistId");
        reportTable1.setColumnInfos(List.of(
            new ReportTableColumn("artistId", "int4"),
            new ReportTableColumn("name", "varchar")
        ));

        final ReportBodyTableResult reportTableResult1 = new ReportBodyTableResult();
        reportTableResult1.setResultId(9);
        reportTableResult1.setCode(200); // 200 / 400
        reportTableResult1.setTable(ReportTableDto.build(reportTable1));

        final RequestEntity<ReportBodyTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-get-table-by-name-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/table/get-table-by-name/Artist?resultId=" + reportTableResult1.getResultId()))
            .build(); // 200 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getTableArtistByName: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getTableArtistByName");
            }
        }
    }

    private void createTableCustomer() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(10);
        reportTableResult1.setCode(201);
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1); // 201 / 406
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable reportTable1 = new ReportTable();
        reportTable1.setTableName("Customer");
        reportTable1.setColumnsAmount(12);
        reportTable1.setPrimaryKey("CustomerId");
        reportTable1.setColumnInfos(List.of(
            new ReportTableColumn("CustomerId", "INT"),
            new ReportTableColumn("FirstName", "VARCHAR(40)"),
            new ReportTableColumn("LastName", "VARCHAR(20)"),
            new ReportTableColumn("Company", "VARCHAR(80)"),
            new ReportTableColumn("Address", "VARCHAR(70)"),
            new ReportTableColumn("City", "VARCHAR(40)"),
            new ReportTableColumn("Country", "VARCHAR(40)"),
            new ReportTableColumn("PostalCode", "VARCHAR(10)"),
            new ReportTableColumn("Phone", "VARCHAR(24)"),
            new ReportTableColumn("Fax", "VARCHAR(24)"),
            new ReportTableColumn("Email", "VARCHAR(60)"),
            new ReportTableColumn("SupportRepId", "int4")
        ));
        final RequestEntity<ReportTable> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/create-table?resultId=" + reportTableResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTable1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableCustomer: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableCustomer");
            }
        }
    }

    private void dropTableCustomer() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(11);
        reportTableResult1.setCode(201); // 201 / 406 | (400 - proxy error)
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-drop-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table/drop-table/Customer?resultId=" + reportTableResult1.getResultId()))
            .build(); // 201 / 406 | (400 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> dropTableCustomer: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: dropTableCustomer");
            }
        }
    }

    private void createTableCustomerAgain() {
        final ReportTableResult reportTableResult1 = new ReportTableResult();
        reportTableResult1.setResultId(12);
        reportTableResult1.setCode(201);
        final RequestEntity<ReportTableResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/add-create-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableResult1); // 201 / 406
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable reportTable1 = new ReportTable();
        reportTable1.setTableName("Customer");
        reportTable1.setColumnsAmount(12);
        reportTable1.setPrimaryKey("CustomerId");
        reportTable1.setColumnInfos(List.of(
            new ReportTableColumn("CustomerId", "INT"),
            new ReportTableColumn("FirstName", "VARCHAR(40)"),
            new ReportTableColumn("LastName", "VARCHAR(20)"),
            new ReportTableColumn("Company", "VARCHAR(80)"),
            new ReportTableColumn("Address", "VARCHAR(70)"),
            new ReportTableColumn("City", "VARCHAR(40)"),
            new ReportTableColumn("Country", "VARCHAR(40)"),
            new ReportTableColumn("PostalCode", "VARCHAR(10)"),
            new ReportTableColumn("Phone", "VARCHAR(24)"),
            new ReportTableColumn("Fax", "VARCHAR(24)"),
            new ReportTableColumn("Email", "VARCHAR(60)"),
            new ReportTableColumn("SupportRepId", "int4")
        ));
        final RequestEntity<ReportTable> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table/create-table?resultId=" + reportTableResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTable1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableCustomerAgain: " + reportTableResult1.getCode() + " VS " + respCode);
            if (reportTableResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableCustomerAgain");
            }
        }
    }

    //endregion TABLE

    //region TABLE QUERY

    private void createTableQueryForAlbumTable() {
        final ReportBodyTableQueryResult reportTableQueryResult1 = new ReportBodyTableQueryResult();
        reportTableQueryResult1.setResultId(13);
        reportTableQueryResult1.setCode(406); // 201 / 406 | (400 - proxy error)
        reportTableQueryResult1.setTableQuery(null);

        final RequestEntity<ReportBodyTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(1);
        tableQuery1.setTableName("Album");
        tableQuery1.setQuery("select * from Album");
        final RequestEntity<ReportTableQuery> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table?resultId=" + reportTableQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(tableQuery1); // 201 / 406 | (400 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableQueryForAlbumTable: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableQueryForAlbumTable");
            }
        }
    }

    private void createTableQueryForArtistTable() {
        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(2);
        tableQuery1.setTableName("Artist");
        tableQuery1.setQuery("select * from Artist");

        final ReportBodyTableQueryResult reportTableQueryResult1 = new ReportBodyTableQueryResult();
        reportTableQueryResult1.setResultId(14);
        reportTableQueryResult1.setCode(201); // 201 / 406
        reportTableQueryResult1.setTableQuery(ReportTableQueryDto.build(tableQuery1));

        final RequestEntity<ReportBodyTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<ReportTableQuery> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table?resultId=" + reportTableQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(tableQuery1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableQueryForArtistTable: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableQueryForArtistTable");
            }
        }
    }

    private void getAllTableQueries() { // FIXME В доке отсутствует!
        final ReportTableQueryResult reportTableQueryResult1 = new ReportTableQueryResult();
        reportTableQueryResult1.setResultId(15);
        reportTableQueryResult1.setCode(200);
        final RequestEntity<ReportTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/get-all-table-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/get-all-table-queries?resultId=" + reportTableQueryResult1.getResultId()))
            .build();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getAllTableQueries: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getAllTableQueries");
            }
        }
    }

    private void createTableQueryForCustomerTable() {
        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(3);
        tableQuery1.setTableName("Customer");
        tableQuery1.setQuery("select * from Customer");

        final ReportBodyTableQueryResult reportTableQueryResult1 = new ReportBodyTableQueryResult();
        reportTableQueryResult1.setResultId(16);
        reportTableQueryResult1.setCode(201); // 201 / 406
        reportTableQueryResult1.setTableQuery(ReportTableQueryDto.build(tableQuery1));

        final RequestEntity<ReportBodyTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<ReportTableQuery> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table?resultId=" + reportTableQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(tableQuery1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableQueryForCustomerTable: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableQueryForCustomerTable");
            }
        }
    }

    private void getAllTableQueriesAgain() { // FIXME В доке отсутствует!
        final ReportTableQueryResult reportTableQueryResult1 = new ReportTableQueryResult();
        reportTableQueryResult1.setResultId(17);
        reportTableQueryResult1.setCode(200);
        final RequestEntity<ReportTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/get-all-table-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/get-all-table-queries?resultId=" + reportTableQueryResult1.getResultId()))
            .build();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getAllTableQueriesAgain: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getAllTableQueriesAgain");
            }
        }
    }

    private void createTableQueryForEmployeeTable() {
        final ReportBodyTableQueryResult reportTableQueryResult1 = new ReportBodyTableQueryResult();
        reportTableQueryResult1.setResultId(18);
        reportTableQueryResult1.setCode(406); // 201 / 406
        reportTableQueryResult1.setTableQuery(null);

        final RequestEntity<ReportBodyTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(4);
        tableQuery1.setTableName("Employee");
        tableQuery1.setQuery("select * from Employee");
        final RequestEntity<ReportTableQuery> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table?resultId=" + reportTableQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(tableQuery1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableQueryForEmployeeTable: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableQueryForEmployeeTable");
            }
        }
    }

    private void createTableQueryForGenreTable() {
        final ReportBodyTableQueryResult reportTableQueryResult1 = new ReportBodyTableQueryResult();
        reportTableQueryResult1.setResultId(19);
        reportTableQueryResult1.setCode(406); // 201 / 406
        reportTableQueryResult1.setTableQuery(null);

        final RequestEntity<ReportBodyTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(4);
        tableQuery1.setTableName("Genre");
        tableQuery1.setQuery("select * from Genre");
        final RequestEntity<ReportTableQuery> reportTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/add-new-query-to-table?resultId=" + reportTableQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(tableQuery1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createTableQueryForGenreTable: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createTableQueryForGenreTable");
            }
        }
    }

    private void getTableQueryByIdForGenre() {
        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(4);
        tableQuery1.setTableName("Genre");
        tableQuery1.setQuery("select * from Genre");

        final ReportBodyTableQueryResult reportTableQueryResult1 = new ReportBodyTableQueryResult();
        reportTableQueryResult1.setResultId(20);
        reportTableQueryResult1.setCode(200); // 200 / 500
        reportTableQueryResult1.setTableQuery(ReportTableQueryDto.build(tableQuery1));

        final RequestEntity<ReportBodyTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/get-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 200 / 500

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/get-table-query-by-id/4?resultId=" + reportTableQueryResult1.getResultId()))
            .build(); // 200 / 500

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getTableQueryByIdForGenre: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getTableQueryByIdForGenre");
            }
        }
    }

    private void executeTableQueryByIdForGenre() {
        final ReportTableQueryResult reportTableQueryResult1 = new ReportTableQueryResult();
        reportTableQueryResult1.setResultId(21);
        reportTableQueryResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/execute-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/execute-table-query-by-id/4?resultId=" + reportTableQueryResult1.getResultId()))
            .build(); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> executeTableQueryByIdForGenre: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: executeTableQueryByIdForGenre");
            }
        }
    }

    private void deleteTableQueryWithQueryId5() {
        final ReportTableQueryResult reportTableQueryResult1 = new ReportTableQueryResult();
        reportTableQueryResult1.setResultId(22);
        reportTableQueryResult1.setCode(406); // 200 / 406
        final RequestEntity<ReportTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/delete-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table-query/delete-table-query-by-id/5?resultId=" + reportTableQueryResult1.getResultId()))
            .build(); // 200 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> deleteTableQueryWithQueryId5: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: deleteTableQueryWithQueryId5");
            }
        }
    }

    private void deleteTableQueryWithQueryId6() {
        final ReportTableQueryResult reportTableQueryResult1 = new ReportTableQueryResult();
        reportTableQueryResult1.setResultId(23);
        reportTableQueryResult1.setCode(406); // 200 / 406
        final RequestEntity<ReportTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/delete-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table-query/delete-table-query-by-id/6?resultId=" + reportTableQueryResult1.getResultId()))
            .build(); // 200 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> deleteTableQueryWithQueryId6: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: deleteTableQueryWithQueryId6");
            }
        }
    }

    private void deleteTableQueryWithQueryId5Again() {
        final ReportTableQueryResult reportTableQueryResult1 = new ReportTableQueryResult();
        reportTableQueryResult1.setResultId(24);
        reportTableQueryResult1.setCode(406); // 200 / 406
        final RequestEntity<ReportTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/delete-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201, 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/table-query/delete-table-query-by-id/5?resultId=" + reportTableQueryResult1.getResultId()))
            .build(); // 200 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> deleteTableQueryWithQueryId5Again: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: deleteTableQueryWithQueryId5Again");
            }
        }
    }

    private void modifyTableQueryWithQueryId4() {
        final ReportTableQueryResult reportTableQueryResult1 = new ReportTableQueryResult();
        reportTableQueryResult1.setResultId(25);
        reportTableQueryResult1.setCode(406); // 200 / 406

        final RequestEntity<ReportTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/modify-table-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(4);
        tableQuery1.setTableName("Employee");
        tableQuery1.setQuery("INSERT INTO Employee (EmployeeId, LastName, FirstName, Title, BirthDate, HireDate, Address, City, State, Country, PostalCode, Phone, Fax, Email) VALUES (1, N'Adams', N'Andrew', N'General Manager', '1962/2/18', '2002/8/14', N'11120 Jasper Ave NW', N'Edmonton', N'AB', N'Canada', N'T5K 2N1', N'+1 (780) 428-9482', N'+1 (780) 428-3457', N'andrew@chinookcorp.com');");
        final RequestEntity<ReportTableQuery> reportTable1_Request = RequestEntity
            .put(URI.create(hostName + "/api/table-query/modify-query-in-table?resultId=" + reportTableQueryResult1.getResultId()))
            .body(tableQuery1); // 200 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> modifyTableQueryWithQueryId4: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: modifyTableQueryWithQueryId4");
            }
        }
    }

    private void getAllQueriesByTableNameForGenre() { // FIXME В доке отсутствует!
        final ReportTableQuery tableQuery1 = new ReportTableQuery();
        tableQuery1.setQueryId(4);
        tableQuery1.setTableName("Genre");
        tableQuery1.setQuery("select * from Genre");

        final ReportBodyListTableQueryResult reportTableQueryResult1 = new ReportBodyListTableQueryResult();
        reportTableQueryResult1.setResultId(26);
        reportTableQueryResult1.setCode(200);
        reportTableQueryResult1.setTableQueries(List.of(ReportTableQueryDto.build(tableQuery1)));
        final RequestEntity<ReportBodyListTableQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/table-query/get-all-queries-by-table-name-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportTableQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/table-query/get-all-queries-by-table-name/Genre?resultId=" + reportTableQueryResult1.getResultId()))
            .build(); // 200

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getAllQueriesByTableNameForGenre: " + reportTableQueryResult1.getCode() + " VS " + respCode);
            if (reportTableQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getAllQueriesByTableNameForGenre");
            }
        }
    }

    //endregion TABLE QUERY

    //region SINGLE QUERY

    private void createSingleQueryForAlbum() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(27);
        reportSingleQueryResult1.setCode(201); // 201 / 400
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportSingleQuery singleQuery1 = new ReportSingleQuery();
        singleQuery1.setQueryId(1);
        singleQuery1.setQuery("select * from Album");
        final RequestEntity<ReportSingleQuery> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query?resultId=" + reportSingleQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(singleQuery1); // 201 / 403

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createSingleQueryForAlbum: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createSingleQueryForAlbum");
            }
        }
    }

    private void getAllSingleQueries() { // FIXME В доке отсутствует!
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(28);
        reportSingleQueryResult1.setCode(200);
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-get-all-single-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/single-query/get-all-single-queries?resultId=" + reportSingleQueryResult1.getResultId()))
            .build();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getAllSingleQueries: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getAllSingleQueries");
            }
        }
    }

    private void createSingleQueryForArtist() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(29);
        reportSingleQueryResult1.setCode(201); // 201 / 400
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportSingleQuery singleQuery1 = new ReportSingleQuery();
        singleQuery1.setQueryId(2);
        singleQuery1.setQuery("select * from Artist");
        final RequestEntity<ReportSingleQuery> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query?resultId=" + reportSingleQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(singleQuery1); // 201 / 403

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createSingleQueryForArtist: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createSingleQueryForArtist");
            }
        }
    }

    private void getAllSingleQueriesAgain() { // FIXME В доке отсутствует!
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(30);
        reportSingleQueryResult1.setCode(200);
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-get-all-single-queries-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/single-query/get-all-single-queries?resultId=" + reportSingleQueryResult1.getResultId()))
            .build();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getAllSingleQueriesAgain: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getAllSingleQueriesAgain");
            }
        }
    }

    private void createSingleQueryForCustomer() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(31);
        reportSingleQueryResult1.setCode(201); // 201 / 400
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportSingleQuery singleQuery1 = new ReportSingleQuery();
        singleQuery1.setQueryId(3);
        singleQuery1.setQuery("select * from Customer");
        final RequestEntity<ReportSingleQuery> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query?resultId=" + reportSingleQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(singleQuery1); // 201 / 403

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createSingleQueryForCustomer: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createSingleQueryForCustomer");
            }
        }
    }

    private void modifySingleQueryForCustomer() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(32);
        reportSingleQueryResult1.setCode(201); // 200 / 406 | (400 - proxy error)
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-modify-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportSingleQuery singleQuery1 = new ReportSingleQuery();
        singleQuery1.setQueryId(3);
        singleQuery1.setQuery("select * from Customer limit 1");
        final RequestEntity<ReportSingleQuery> reportSingleTable1_Request = RequestEntity
            .put(URI.create(hostName + "/api/single-query/modify-single-query?resultId=" + reportSingleQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(singleQuery1); // 200 / 406 | (400 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> modifySingleQueryForCustomer: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: modifySingleQueryForCustomer");
            }
        }
    }

    private void executeSingleQueryForCustomer() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(33);
        reportSingleQueryResult1.setCode(201); // 201 / 406 | (400 - proxy error)
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-execute-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/single-query/execute-single-query-by-id/3?resultId=" + reportSingleQueryResult1.getResultId()))
            .build(); // 201 / 406 | (400 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> executeSingleQueryForCustomer: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: executeSingleQueryForCustomer");
            }
        }
    }

    private void createSingleQueryForEmployee() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(34);
        reportSingleQueryResult1.setCode(201); // 201 / 400
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportSingleQuery singleQuery1 = new ReportSingleQuery();
        singleQuery1.setQueryId(4);
        singleQuery1.setQuery("select * from Employee");
        final RequestEntity<ReportSingleQuery> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query?resultId=" + reportSingleQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(singleQuery1); // 201 / 403

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createSingleQueryForEmployee: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createSingleQueryForEmployee");
            }
        }
    }

    private void createSingleQueryForGenre() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(35);
        reportSingleQueryResult1.setCode(201); // 201 / 400
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 201 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportSingleQuery singleQuery1 = new ReportSingleQuery();
        singleQuery1.setQueryId(5);
        singleQuery1.setQuery("select * from Genre");
        final RequestEntity<ReportSingleQuery> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-new-query?resultId=" + reportSingleQueryResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(singleQuery1); // 201 / 403

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createSingleQueryForGenre: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createSingleQueryForGenre");
            }
        }
    }

    private void getSingleQueryForGenreById() {
        final ReportBodySingleQueryResult reportSingleQueryResult1 = new ReportBodySingleQueryResult();
        reportSingleQueryResult1.setResultId(36);
        reportSingleQueryResult1.setCode(200); // 200 / 400 | (500 - proxy error)
        reportSingleQueryResult1.setQueryId(5);
        reportSingleQueryResult1.setQuery("select * from Genre");
        final RequestEntity<ReportBodySingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-get-single-query-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/single-query/get-single-query-by-id/5?resultId=" + reportSingleQueryResult1.getResultId()))
            .build(); // 200 / 400 | (500 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getSingleQueryForGenreById: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getSingleQueryForGenreById");
            }
        }
    }

    private void deleteSingleQueryForGenre() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(37);
        reportSingleQueryResult1.setCode(202); // 202 / 406 | (400 - proxy error)
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-delete-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/single-query/delete-single-query-by-id/5?resultId=" + reportSingleQueryResult1.getResultId()))
            .build(); // 202 / 406 | (400 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> deleteSingleQueryForQueryId5: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: deleteSingleQueryForQueryId5");
            }
        }
    }

    private void deleteSingleQueryForQueryId6() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(38);
        reportSingleQueryResult1.setCode(406); // 202 / 406 | (400 - proxy error)
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-delete-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/single-query/delete-single-query-by-id/6?resultId=" + reportSingleQueryResult1.getResultId()))
            .build(); // 202 / 406 | (400 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> deleteSingleQueryForQueryId6: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: deleteSingleQueryForQueryId6");
            }
        }
    }

    private void deleteSingleQueryForQueryId5Again() {
        final ReportSingleQueryResult reportSingleQueryResult1 = new ReportSingleQueryResult();
        reportSingleQueryResult1.setResultId(39);
        reportSingleQueryResult1.setCode(406); // 202 / 406 | (400 - proxy error)
        final RequestEntity<ReportSingleQueryResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/single-query/add-delete-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportSingleQueryResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .delete(URI.create(hostName + "/api/single-query/delete-single-query-by-id/5?resultId=" + reportSingleQueryResult1.getResultId()))
            .build(); // 202 / 406 | (400 - proxy error)

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> deleteSingleQueryForQueryId5Again: " + reportSingleQueryResult1.getCode() + " VS " + respCode);
            if (reportSingleQueryResult1.getCode() != respCode) {
                LOGGER.error("ERROR: deleteSingleQueryForQueryId5Again");
            }
        }
    }

    //endregion SINGLE QUERY

    //region REPORT

    private void createReportForArtists() {
        final ReportResult reportResult1 = new ReportResult();
        reportResult1.setResultId(40);
        reportResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/report/add-create-report-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable table1 = new ReportTable();
        table1.setTableName("Artists");
        table1.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("age", "int4")
        ));
        final ReportCreateDto report1 = new ReportCreateDto();
        report1.setReportId(1);
        report1.setTableAmount(1);
        report1.setTables(List.of(
            ReportCreateDto.ReportCreateTableDto.build(table1)
        ));

        final RequestEntity<ReportCreateDto> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/report/create-report?resultId=" + reportResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(report1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createReportForArtists: " + reportResult1.getCode() + " VS " + respCode);
            if (reportResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createReportForArtists");
            }
        }
    }

    private void getReportForArtists() {
        final ReportTable table1 = new ReportTable();
        table1.setTableName("Artists");
        table1.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("age", "int4")
        ));
        final ReportReadDto report1 = new ReportReadDto();
        report1.setReportId(1);
        report1.setTableAmount(1);
        report1.setTables(List.of(ReportReadDto.ReportReadTableDto.build(table1)));

        final ReportBodyResult reportResult1 = new ReportBodyResult();
        reportResult1.setResultId(41);
        reportResult1.setCode(201); // 201 / 406
        reportResult1.setGetReport(report1);
        final RequestEntity<ReportBodyResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/report/get-report-by-id-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final RequestEntity<Void> reportSingleTable1_Request = RequestEntity
            .get(URI.create(hostName + "/api/report/get-report-by-id/1?resultId=" + reportResult1.getResultId()))
            .build(); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> getReportForArtists: " + reportResult1.getCode() + " VS " + respCode);
            if (reportResult1.getCode() != respCode) {
                LOGGER.error("ERROR: getReportForArtists");
            }
        }
    }

    private void createReportForArtistsAndJob() {
        final ReportResult reportResult1 = new ReportResult();
        reportResult1.setResultId(42);
        reportResult1.setCode(406); // 201 / 406
        final RequestEntity<ReportResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/report/add-create-report-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable table1 = new ReportTable();
        table1.setTableName("Artists");
        table1.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("age", "int4")
        ));
        final ReportTable table2 = new ReportTable();
        table2.setTableName("Job");
        table2.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("salary", "int4"),
            new ReportTableColumn("address", "varchar")
        ));
        final ReportCreateDto report1 = new ReportCreateDto();
        report1.setReportId(2);
        report1.setTableAmount(2);
        report1.setTables(List.of(
            ReportCreateDto.ReportCreateTableDto.build(table1),
            ReportCreateDto.ReportCreateTableDto.build(table2)
        ));

        final RequestEntity<ReportCreateDto> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/report/create-report?resultId=" + reportResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(report1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createReportForArtistsAndJob: " + reportResult1.getCode() + " VS " + respCode);
            if (reportResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createReportForArtistsAndJob");
            }
        }
    }

    private void createReportForArtistsAndArtist() {
        final ReportResult reportResult1 = new ReportResult();
        reportResult1.setResultId(43);
        reportResult1.setCode(201); // 201 / 406
        final RequestEntity<ReportResult> reportTableResult1_Request = RequestEntity
            .post(URI.create(hostName + "/api/report/add-create-report-result"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportResult1);
        final ResponseEntity<Void> reportTableResult1_Response = restTemplate.exchange(reportTableResult1_Request, Void.class); // 202 / 400

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RESULT | REQ: {} RESP: {}", reportTableResult1_Request, reportTableResult1_Response);
        }

        final ReportTable table1 = new ReportTable();
        table1.setTableName("Artists");
        table1.setColumnInfos(List.of(
            new ReportTableColumn("id", "int4"),
            new ReportTableColumn("name", "varchar"),
            new ReportTableColumn("age", "int4")
        ));
        final ReportTable table2 = new ReportTable();
        table2.setTableName("Artist");
        table2.setColumnInfos(List.of(
            new ReportTableColumn("artistId", "int4"),
            new ReportTableColumn("name", "varchar")
        ));
        final ReportCreateDto report1 = new ReportCreateDto();
        report1.setReportId(3);
        report1.setTableAmount(2);
        report1.setTables(List.of(
            ReportCreateDto.ReportCreateTableDto.build(table1),
            ReportCreateDto.ReportCreateTableDto.build(table2)
        ));

        final RequestEntity<ReportCreateDto> reportSingleTable1_Request = RequestEntity
            .post(URI.create(hostName + "/api/report/create-report?resultId=" + reportResult1.getResultId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(report1); // 201 / 406

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ACTION | REQ: {}", reportSingleTable1_Request);
        }

        int respCode;
        try {
            final ResponseEntity<String> reportTable1_Response = restTemplate.exchange(reportSingleTable1_Request, String.class);
            respCode = reportTable1_Response.getStatusCodeValue();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", reportTable1_Response);
            }

        } catch (HttpStatusCodeException ex) {
            respCode = ex.getRawStatusCode();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ACTION | RESP: {}", ex.toString());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>> createReportForArtistsAndArtist: " + reportResult1.getCode() + " VS " + respCode);
            if (reportResult1.getCode() != respCode) {
                LOGGER.error("ERROR: createReportForArtistsAndArtist");
            }
        }
    }

    //endregion REPORT
}
