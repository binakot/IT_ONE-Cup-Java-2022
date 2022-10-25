package com.example.demo.controller;

import com.example.demo.model.dto.ReportTableDto;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableResult;
import com.example.demo.service.ReportTableResultService;
import com.example.demo.service.ReportTableService;
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
@RequestMapping("/api/table")
public class ReportTableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTableController.class);

    private final ReportTableService reportTableService;
    private final ReportTableResultService reportTableResultService;

    @Autowired
    public ReportTableController(final ReportTableService reportTableService,
                                 final ReportTableResultService reportTableResultService) {
        this.reportTableService = reportTableService;
        this.reportTableResultService = reportTableResultService;
    }

    //region REPORT BUILDER

    @RequestMapping(value = "/create-table", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody @Validated @NotNull final ReportTable reportTable,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("T.CREATE.REQ: " + reportTable);
            }

            final ReportTable createdReportTable = reportTableService.createTable(reportTable);
            final ResponseEntity<Object> response = ResponseEntity
                .created(URI.create("/api/table/get-table-by-name/" + createdReportTable.getTableName()))
                .build();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("T.CREATE.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.CREATE.REQ: " + reportTable + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportTableResult> storedResult = reportTableResultService.getTableResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TR.CREATE.REQ: " + response);
                }
                return response;
            }
            reportTable.setResult(storedResult.get());

            final ReportTable createdReportTable = reportTableService.createTable(reportTable);
            final ResponseEntity<Object> response = ResponseEntity
                .created(URI.create("/api/table/get-table-by-name/" + createdReportTable.getTableName()))
                .build();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.CREATE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/get-table-by-name/{name}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByName(@PathVariable("name") @NotNull final String tableName,
                                       @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("T.GET.REQ: " + tableName);
            }

            return reportTableService.getByTableName(tableName)
                .map(t -> {
                    final ResponseEntity<Object> response = ResponseEntity.ok(ReportTableDto.build(t));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("T.GET.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("T.GET.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.GET.REQ: " + tableName + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportTableResult> storedResult = reportTableResultService.getTableResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TR.GET.REQ: " + response);
                }
                return response;
            }

            return reportTableService.getByTableName(tableName)
                .map(t -> {
                    t.setResult(storedResult.get());
                    reportTableService.createTable(t);

                    final ResponseEntity<Object> response = ResponseEntity.ok(ReportTableDto.build(t));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TR.GET.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TR.GET.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/drop-table-by-name/{name}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteByName(@PathVariable("name") @NotNull final String tableName,
                                          @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("T.DEL.REQ: " + tableName);
            }

            return reportTableService.getByTableName(tableName)
                .map(t -> {
                    reportTableService.deleteByTableName(t.getTableName());
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("T.DEL.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("T.DEL.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.DEL.REQ: " + tableName + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportTableResult> storedResult = reportTableResultService.getTableResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TR.DEL.REQ: " + response);
                }
                return response;
            }

            return reportTableService.getByTableName(tableName)
                .map(t -> {
                    //reportTableService.deleteByTableName(t.getTableName()); Реально не удаляем таблицу.
                    t.setResult(storedResult.get());
                    reportTableService.createTable(t);

                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TR.DEL.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TR.DEL.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/drop-table/{name}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteVer2(@PathVariable("name") @NotNull final String tableName,
                                        @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("T.DEL.REQ: " + tableName);
            }

            return reportTableService.getByTableName(tableName)
                .map(t -> {
                    reportTableService.deleteByTableName(t.getTableName());
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("T.DEL.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("T.DEL.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.DEL.REQ: " + tableName + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportTableResult> storedResult = reportTableResultService.getTableResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TR.DEL.REQ: " + response);
                }
                return response;
            }

            return reportTableService.getByTableName(tableName)
                .map(t -> {
                    //reportTableService.deleteByTableName(t.getTableName()); Реально не удаляем таблицу.
                    t.setResult(storedResult.get());
                    reportTableService.createTable(t);

                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TR.DEL.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TR.DEL.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    //endregion REPORT BUILDER

    //region CHECKER

    @RequestMapping(value = "/add-create-table-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCreateTableResult(@RequestBody @Validated @NotNull final ReportTableResult reportTableResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TR.CREATE.REQ: " + reportTableResult);
        }

//        if (reportTableResult.getCode() != 201 && reportTableResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableResultService.createTableResult(reportTableResult);
            final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.CREATE.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.CREATE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-get-table-by-name-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGetTableResult(@RequestBody @Validated @NotNull final ReportTableResult reportTableResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TR.GET.REQ: " + reportTableResult);
        }

//        if (reportTableResult.getCode() != 200) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableResultService.createTableResult(reportTableResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.GET.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.GET.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-drop-table-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> creatDropTableResult(@RequestBody @Validated @NotNull final ReportTableResult reportTableResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TR.DEL.REQ: " + reportTableResult);
        }

//        if (reportTableResult.getCode() != 201 && reportTableResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableResultService.createTableResult(reportTableResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.DEL.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TR.DEL.RESP: " + response);
            }
            return response;
        }
    }

    //endregion CHECKER
}
