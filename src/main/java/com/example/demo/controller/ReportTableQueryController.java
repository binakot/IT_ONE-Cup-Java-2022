package com.example.demo.controller;

import com.example.demo.model.dto.ReportTableQueryDto;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.model.entity.ReportTableQueryResult;
import com.example.demo.service.ReportTableQueryResultService;
import com.example.demo.service.ReportTableQueryService;
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
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/table-query")
public class ReportTableQueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTableQueryController.class);

    private final ReportTableService reportTableService;
    private final ReportTableQueryService reportTableQueryService;
    private final ReportTableQueryResultService reportTableQueryResultService;

    @Autowired
    public ReportTableQueryController(final ReportTableService reportTableService,
                                      final ReportTableQueryService reportTableQueryService,
                                      final ReportTableQueryResultService reportTableQueryResultService) {
        this.reportTableService = reportTableService;
        this.reportTableQueryService = reportTableQueryService;
        this.reportTableQueryResultService = reportTableQueryResultService;
    }

    //region REPORT BUILDER

    @RequestMapping(value = "/add-new-query-to-table", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody @Validated @NotNull final ReportTableQuery reportTableQuery,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.CREATE.REQ: " + reportTableQuery);
            }

            final Optional<ReportTable> table = reportTableService.getByTableName(reportTableQuery.getTableName());
            if (table.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQ.CREATE.RESP: " + response);
                }
                return response;
            }

            final ReportTableQuery createdQuery = reportTableQueryService.createTableQuery(reportTableQuery);
            final ResponseEntity<Object> response = ResponseEntity
                .created(URI.create("/api/table-query/get-table-query-by-id/" + createdQuery.getQueryId()))
                .build();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.CREATE.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.CREATE.REQ: " + reportTableQuery + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportTable> table = reportTableService.getByTableName(reportTableQuery.getTableName());
            if (table.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.CREATE.RESP: " + response);
                }
                return response;
            }

            final Optional<ReportTableQueryResult> storedResult = reportTableQueryResultService.getTableQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.CREATE.REQ: " + response);
                }
                return response;
            }
            reportTableQuery.setResult(storedResult.get());

            final ReportTableQuery createdQuery = reportTableQueryService.createTableQuery(reportTableQuery);
            final ResponseEntity<Object> response = ResponseEntity
                .created(URI.create("/api/table-query/get-table-query-by-id/" + createdQuery.getQueryId()))
                .build();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.CREATE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/modify-query-in-table", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @NotNull @Validated final ReportTableQuery reportTableQuery,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.MODIFY.REQ: " + reportTableQuery);
            }

            final Optional<ReportTable> table = reportTableService.getByTableName(reportTableQuery.getTableName());
            if (table.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQ.MODIFY.RESP: " + response);
                }
                return response;
            }

            return reportTableQueryService.getByQueryId(reportTableQuery.getQueryId())
                .map(q -> {
                    reportTableQueryService.createTableQuery(reportTableQuery);
                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.MODIFY.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.MODIFY.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.MODIFY.REQ: " + reportTableQuery + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportTable> table = reportTableService.getByTableName(reportTableQuery.getTableName());
            if (table.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.MODIFY.RESP: " + response);
                }
                return response;
            }

            final Optional<ReportTableQueryResult> storedResult = reportTableQueryResultService.getTableQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.MODIFY.REQ: " + response);
                }
                return response;
            }
            reportTableQuery.setResult(storedResult.get());

            return reportTableQueryService.getByQueryId(reportTableQuery.getQueryId())
                .map(q -> {
                    reportTableQueryService.createTableQuery(reportTableQuery);
                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.MODIFY.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.MODIFY.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/delete-table-query-by-id/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull final Integer queryId,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.DELETE.REQ: " + queryId);
            }

            return reportTableQueryService.getByQueryId(queryId)
                .map(q -> {
                    reportTableQueryService.deleteByQueryId(queryId);
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.ACCEPTED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.DELETE.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.DELETE.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.DELETE.REQ: " + queryId);
            }

            final Optional<ReportTableQueryResult> storedResult = reportTableQueryResultService.getTableQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.DELETE.REQ: " + response);
                }
                return response;
            }

            return reportTableQueryService.getByQueryId(queryId)
                .map(q -> {
                    //reportTableQueryService.deleteByQueryId(queryId); Реально не удаляем запрос.
                    q.setResult(storedResult.get());
                    reportTableQueryService.createTableQuery(q);

                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.DELETE.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.DELETE.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/execute-table-query-by-id/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> execute(@PathVariable("id") @NotNull final Integer queryId,
                                     @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.EXEC.REQ: " + queryId);
            }

            return reportTableQueryService.getByQueryId(queryId)
                .map(q -> {
                    reportTableQueryService.execute(q);
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.EXEC.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.EXEC.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.EXEC.REQ: " + queryId);
            }

            final Optional<ReportTableQueryResult> storedResult = reportTableQueryResultService.getTableQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.EXEC.REQ: " + response);
                }
                return response;
            }

            return reportTableQueryService.getByQueryId(queryId)
                .map(q -> {
                    //reportTableQueryService.execute(q); Реально не выполняем запрос.
                    q.setResult(storedResult.get());
                    reportTableQueryService.createTableQuery(q);

                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.EXEC.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.EXEC.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/get-all-queries-by-table-name/{name}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllQueriesByTableName(@PathVariable("name") @NotNull final String tableName,
                                                      @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.ALL.REQ: " + tableName);
            }

            final Optional<ReportTable> table = reportTableService.getByTableName(tableName);
            if (table.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.ok().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQ.ALL.RESP: " + response);
                }
                return response;
            }
            final ResponseEntity<Object> response = ResponseEntity.ok(reportTableQueryService.getAllByTableName(tableName).stream()
                .map(ReportTableQueryDto::build)
                .collect(Collectors.toList()));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.ALL.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.REQ: " + tableName);
            }

            final Optional<ReportTable> table = reportTableService.getByTableName(tableName);
            if (table.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.ok().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.ALL.RESP: " + response);
                }
                return response;
            }

            final Optional<ReportTableQueryResult> storedResult = reportTableQueryResultService.getTableQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.ALL.REQ: " + response);
                }
                return response;
            }

            reportTableQueryService.getAllByTableName(tableName).forEach(q -> {
                q.setResult(storedResult.get());
                reportTableQueryService.createTableQuery(q);
            });

            final ResponseEntity<Object> response = ResponseEntity.ok(reportTableQueryService.getAllByTableName(tableName).stream()
                .map(ReportTableQueryDto::build)
                .collect(Collectors.toList()));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/get-table-query-by-id/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByQueryId(@PathVariable("id") @NotNull final Integer queryId,
                                          @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.GET.REQ: " + queryId);
            }

            return reportTableQueryService.getByQueryId(queryId)
                .map(q -> {
                    final ResponseEntity<Object> response = ResponseEntity.ok(ReportTableQueryDto.build(q));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.GET.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQ.GET.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.GET.REQ: " + queryId);
            }

            final Optional<ReportTableQueryResult> storedResult = reportTableQueryResultService.getTableQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.GET.REQ: " + response);
                }
                return response;
            }

            return reportTableQueryService.getByQueryId(queryId)
                .map(q -> {
                    q.setResult(storedResult.get());
                    reportTableQueryService.createTableQuery(q);

                    final ResponseEntity<Object> response = ResponseEntity.ok(ReportTableQueryDto.build(q));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.GET.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("TQR.GET.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/get-all-table-queries", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.ALL.REQ");
            }

            final ResponseEntity<Object> response = ResponseEntity.ok(reportTableQueryService.getAll().stream()
                .map(ReportTableQueryDto::build)
                .collect(Collectors.toList()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQ.ALL.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.REQ");
            }

            final Optional<ReportTableQueryResult> storedResult = reportTableQueryResultService.getTableQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TQR.ALL.REQ: " + response);
                }
                return response;
            }

            reportTableQueryService.getAll().forEach(q -> {
                q.setResult(storedResult.get());
                reportTableQueryService.createTableQuery(q);
            });

            final ResponseEntity<Object> response = ResponseEntity.ok(reportTableQueryService.getAll().stream()
                .map(ReportTableQueryDto::build)
                .collect(Collectors.toList()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.RESP: " + response);
            }
            return response;
        }
    }

    //endregion REPORT BUILDER

    //region CHECKER

    @RequestMapping(value = "/add-new-query-to-table-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAddTableQueryResult(@RequestBody @Validated @NotNull final ReportTableQueryResult reportTableQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TQR.CREATE.REQ: " + reportTableQueryResult);
        }

//        if (reportTableQueryResult.getCode() != 201 && reportTableQueryResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableQueryResultService.createTableQueryResult(reportTableQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.CREATE.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.CREATE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/modify-query-in-table-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createModifyTableQueryResult(@RequestBody @Validated @NotNull final ReportTableQueryResult reportTableQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TQR.MODIFY.REQ: " + reportTableQueryResult);
        }

//        if (reportTableQueryResult.getCode() != 201 && reportTableQueryResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableQueryResultService.createTableQueryResult(reportTableQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.MODIFY.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.MODIFY.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/delete-table-query-by-id-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDeleteTableQueryResult(@RequestBody @Validated @NotNull final ReportTableQueryResult reportTableQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TQR.DELETE.REQ: " + reportTableQueryResult);
        }

//        if (reportTableQueryResult.getCode() != 201 && reportTableQueryResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableQueryResultService.createTableQueryResult(reportTableQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.DELETE.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.DELETE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/execute-table-query-by-id-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createExecuteTableQueryResult(@RequestBody @Validated @NotNull final ReportTableQueryResult reportTableQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TQR.EXEC.REQ: " + reportTableQueryResult);
        }

//        if (reportTableQueryResult.getCode() != 201 && reportTableQueryResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableQueryResultService.createTableQueryResult(reportTableQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.EXEC.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.EXEC.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/get-all-queries-by-table-name-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGetAllTableQueriesByTableNameResult(@RequestBody @Validated @NotNull final ReportTableQueryResult reportTableQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TQR.ALL.REQ: " + reportTableQueryResult);
        }

//        if (reportTableQueryResult.getCode() != 200) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableQueryResultService.createTableQueryResult(reportTableQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/get-table-query-by-id-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGetTableQueryResult(@RequestBody @Validated @NotNull final ReportTableQueryResult reportTableQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TQR.GET.REQ: " + reportTableQueryResult);
        }

//        if (reportTableQueryResult.getCode() != 200 && reportTableQueryResult.getCode() != 500) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportTableQueryResultService.createTableQueryResult(reportTableQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.GET.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.GET.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/get-all-table-queries-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGetAllTableQueriesResult(@RequestBody @Validated @NotNull final ReportTableQueryResult reportTableQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TQR.ALL.REQ: " + reportTableQueryResult);
        }

        try {
            reportTableQueryResultService.createTableQueryResult(reportTableQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TQR.ALL.RESP: " + response);
            }
            return response;
        }
    }

    //endregion CHECKER
}
