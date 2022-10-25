package com.example.demo.controller;

import com.example.demo.model.dto.ReportCreateDto;
import com.example.demo.model.dto.ReportReadDto;
import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.ReportResult;
import com.example.demo.service.ReportResultService;
import com.example.demo.service.ReportService;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;
    private final ReportResultService reportResultService;

    @Autowired
    public ReportController(final ReportService reportService,
                            final ReportResultService reportResultService) {
        this.reportService = reportService;
        this.reportResultService = reportResultService;
    }

    //region REPORT BUILDER

    @RequestMapping(value = "/get-report-by-id/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") @NotNull final Integer reportId,
                                 @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("R.GET.REQ: " + reportId);
            }

            final ResponseEntity<ReportReadDto> response = reportService.getByReportId(reportId)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("R.GET.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.GET.REQ: " + reportId);
            }

            final Optional<ReportResult> storedResult = reportResultService.getReportResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("RR.GET.REQ: " + response);
                }
                return response;
            }

            final ResponseEntity<ReportReadDto> response = reportService.getByReportId(reportId)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.GET.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/create-report", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody @Validated @NotNull final ReportCreateDto reportCreateDto,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("R.CREATE.REQ: " + reportCreateDto);
            }

            final Report createdReport = reportService.createReport(reportCreateDto);
            final ResponseEntity<Object> response = ResponseEntity
                .created(URI.create("/api/report/get-report-by-id/" + createdReport.getReportId()))
                .build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("R.CREATE.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.CREATE.REQ: " + reportCreateDto);
            }

            final Optional<ReportResult> storedResult = reportResultService.getReportResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("RR.CREATE.REQ: " + response);
                }
                return response;
            }

            final Report createdReport = reportService.createReport(reportCreateDto);
            final ResponseEntity<Object> response = ResponseEntity
                .created(URI.create("/api/report/get-report-by-id/" + createdReport.getReportId()))
                .build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.CREATE.RESP: " + response);
            }
            return response;
        }
    }

    //endregion REPORT BUILDER

    //region CHECKER

    @RequestMapping(value = "/add-get-report-by-id-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGetReportResult(@RequestBody @Validated @NotNull final ReportResult reportResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RR.GET.REQ: " + reportResult);
        }

        try {
            reportResultService.createReportResult(reportResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.GET.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.GET.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-create-report-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReportResult(@RequestBody @Validated @NotNull final ReportResult reportResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RR.CREATE.REQ: " + reportResult);
        }

        try {
            reportResultService.createReportResult(reportResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.CREATE.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RR.CREATE.RESP: " + response);
            }
            return response;
        }
    }

    //endregion CHECKER
}
