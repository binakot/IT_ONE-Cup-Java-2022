package com.example.demo.controller;

import com.example.demo.model.dto.ReportSingleQueryDto;
import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportSingleQueryResult;
import com.example.demo.service.ReportSingleQueryResultService;
import com.example.demo.service.ReportSingleQueryService;
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
@RequestMapping("/api/single-query")
public class ReportSingleQueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportSingleQueryController.class);

    private final ReportSingleQueryService reportSingleQueryService;
    private final ReportSingleQueryResultService reportSingleQueryResultService;

    @Autowired
    public ReportSingleQueryController(final ReportSingleQueryService reportSingleQueryService,
                                       final ReportSingleQueryResultService reportSingleQueryResultService) {
        this.reportSingleQueryService = reportSingleQueryService;
        this.reportSingleQueryResultService = reportSingleQueryResultService;
    }

    //region REPORT BUILDER

    @RequestMapping(value = "/add-new-query", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody @Validated @NotNull final ReportSingleQuery reportSingleQuery,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.CREATE.REQ: " + reportSingleQuery);
            }

            try {
                final ReportSingleQuery createdQuery = reportSingleQueryService.createSingleQuery(reportSingleQuery);
                final ResponseEntity<Object> response = ResponseEntity
                    .created(URI.create("/api/single-query/get-single-query-by-id/" + createdQuery.getQueryId()))
                    .build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQ.CREATE.RESP: " + response);
                }
                return response;
            } catch (Exception ex) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQ.CREATE.RESP: " + response);
                }
                return response;
            }
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.CREATE.REQ: " + reportSingleQuery + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportSingleQueryResult> storedResult = reportSingleQueryResultService.getSingleQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.CREATE.REQ: " + response);
                }
                return response;
            }
            reportSingleQuery.setResult(storedResult.get());

            try {
                final ReportSingleQuery createdQuery = reportSingleQueryService.createSingleQuery(reportSingleQuery);
                final ResponseEntity<Object> response = ResponseEntity
                    .created(URI.create("/api/single-query/get-single-query-by-id/" + createdQuery.getQueryId()))
                    .build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.CREATE.REQ: " + response);
                }
                return response;
            } catch (Exception ex) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.CREATE.REQ: " + response);
                }
                return response;
            }
        }
    }

    @RequestMapping(value = "/modify-query", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @Validated @NotNull final ReportSingleQuery reportSingleQuery,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.MODIFY.REQ: " + reportSingleQuery);
            }

            return reportSingleQueryService.getByQueryId(reportSingleQuery.getQueryId())
                .map(q -> {
                    reportSingleQueryService.createSingleQuery(reportSingleQuery);
                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.MODIFY.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.MODIFY.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.MODIFY.REQ: " + reportSingleQuery + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportSingleQueryResult> storedResult = reportSingleQueryResultService.getSingleQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.MODIFY.REQ: " + response);
                }
                return response;
            }
            reportSingleQuery.setResult(storedResult.get());

            return reportSingleQueryService.getByQueryId(reportSingleQuery.getQueryId())
                .map(q -> {
                    //reportSingleQueryService.createSingleQuery(reportSingleQuery); Реально не обновляем запрос.
                    q.setResult(storedResult.get());
                    reportSingleQueryService.createSingleQuery(q);

                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.MODIFY.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.MODIFY.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/modify-single-query", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVer2(@RequestBody @Validated @NotNull final ReportSingleQuery reportSingleQuery,
                                        @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.MODIFY.REQ: " + reportSingleQuery);
            }

            return reportSingleQueryService.getByQueryId(reportSingleQuery.getQueryId())
                .map(q -> {
                    reportSingleQueryService.createSingleQuery(reportSingleQuery);
                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.MODIFY.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.MODIFY.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.MODIFY.REQ: " + reportSingleQuery + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportSingleQueryResult> storedResult = reportSingleQueryResultService.getSingleQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.MODIFY.REQ: " + response);
                }
                return response;
            }

            return reportSingleQueryService.getByQueryId(reportSingleQuery.getQueryId())
                .map(q -> {
                    //reportSingleQueryService.createSingleQuery(reportSingleQuery); Реально не обновляем запрос.
                    q.setResult(storedResult.get());
                    reportSingleQueryService.createSingleQuery(q);

                    final ResponseEntity<Object> response = ResponseEntity.ok().build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.MODIFY.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.MODIFY.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/delete-single-query-by-id/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull final Integer queryId,
                                    @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.DELETE.REQ: " + queryId);
            }

            final Optional<ReportSingleQuery> singleQuery = reportSingleQueryService.getByQueryId(queryId);
            if (singleQuery.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQ.DELETE.RESP: " + response);
                }
                return response;
            }

            reportSingleQueryService.deleteByQueryId(queryId);
            final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.ACCEPTED).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.DELETE.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.DELETE.REQ: " + queryId + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportSingleQueryResult> storedResult = reportSingleQueryResultService.getSingleQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.DELETE.REQ: " + response);
                }
                return response;
            }

            final Optional<ReportSingleQuery> reportSingleQuery = reportSingleQueryService.getByQueryId(queryId);
            if (reportSingleQuery.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.DELETE.RESP: " + response);
                }
                return response;
            }

            //reportSingleQueryService.deleteByQueryId(queryId); Реально не удаляем запрос.
            reportSingleQuery.get().setResult(storedResult.get());
            reportSingleQueryService.createSingleQuery(reportSingleQuery.get());

            final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.ACCEPTED).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.DELETE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/execute-single-query-by-id/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> execute(@PathVariable("id") @NotNull final Integer queryId,
                                     @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.EXEC.REQ: " + queryId);
            }

            return reportSingleQueryService.getByQueryId(queryId)
                .map(q -> {
                    reportSingleQueryService.execute(q);
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.EXEC.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.EXEC.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.EXEC.REQ: " + queryId + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportSingleQueryResult> storedResult = reportSingleQueryResultService.getSingleQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.EXEC.REQ: " + response);
                }
                return response;
            }

            return reportSingleQueryService.getByQueryId(queryId)
                .map(q -> {
                    //reportSingleQueryService.execute(q); Реально не выполняем запрос.
                    q.setResult(storedResult.get());
                    reportSingleQueryService.createSingleQuery(q);

                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.EXEC.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.EXEC.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/get-single-query-by-id/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByQueryId(@PathVariable("id") @NotNull final Integer queryId,
                                          @RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.GET.REQ: " + queryId);
            }

            return reportSingleQueryService.getByQueryId(queryId)
                .map(q -> {
                    final ResponseEntity<Object> response = ResponseEntity.ok(ReportSingleQueryDto.build(q));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.GET.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQ.GET.RESP: " + response);
                    }
                    return response;
                });
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.GET.REQ: " + queryId + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportSingleQueryResult> storedResult = reportSingleQueryResultService.getSingleQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.GET.REQ: " + response);
                }
                return response;
            }

            return reportSingleQueryService.getByQueryId(queryId)
                .map(q -> {
                    q.setResult(storedResult.get());
                    reportSingleQueryService.createSingleQuery(q);

                    final ResponseEntity<Object> response = ResponseEntity.ok(ReportSingleQueryDto.build(q));
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.GET.RESP: " + response);
                    }
                    return response;
                })
                .orElseGet(() -> {
                    final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SQR.GET.RESP: " + response);
                    }
                    return response;
                });
        }
    }

    @RequestMapping(value = "/get-all-single-queries", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(value = "resultId", required = false) final Integer resultId) {
        if (resultId == null) { // REPORT BUILDER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.ALL.REQ");
            }

            final ResponseEntity<Object> response = ResponseEntity.ok(reportSingleQueryService.getAll().stream()
                .map(ReportSingleQueryDto::build)
                .collect(Collectors.toList()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQ.ALL.RESP: " + response);
            }
            return response;
        } else { // CHECKER
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.ALL.REQ" + " | RESULT_ID: " + resultId);
            }

            final Optional<ReportSingleQueryResult> storedResult = reportSingleQueryResultService.getSingleQueryResultByResultId(resultId);
            if (storedResult.isEmpty()) {
                final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SQR.ALL.REQ: " + response);
                }
                return response;
            }

            reportSingleQueryService.getAll().forEach(q -> {
                q.setResult(storedResult.get());
                reportSingleQueryService.createSingleQuery(q);
            });

            final ResponseEntity<Object> response = ResponseEntity.ok(reportSingleQueryService.getAll().stream()
                .map(ReportSingleQueryDto::build)
                .collect(Collectors.toList()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.ALL.RESP: " + response);
            }
            return response;
        }
    }

    //endregion REPORT BUILDER

    //region CHECKER

    @RequestMapping(value = "/add-new-query-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAddSingleQueryResult(@RequestBody @Validated @NotNull final ReportSingleQueryResult reportSingleQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SQR.CREATE.REQ: " + reportSingleQueryResult);
        }

//        if (reportSingleQueryResult.getCode() != 201 && reportSingleQueryResult.getCode() != 400) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportSingleQueryResultService.createSingleQueryResult(reportSingleQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.CREATE.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.CREATE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-modify-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createModifySingleQueryResult(@RequestBody @Validated @NotNull final ReportSingleQueryResult reportSingleQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SQR.MODIFY.REQ: " + reportSingleQueryResult);
        }

//        if (reportSingleQueryResult.getCode() != 200 && reportSingleQueryResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportSingleQueryResultService.createSingleQueryResult(reportSingleQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.MODIFY.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.MODIFY.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-delete-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDeleteSingleQueryResult(@RequestBody @Validated @NotNull final ReportSingleQueryResult reportSingleQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SQR.DELETE.REQ: " + reportSingleQueryResult);
        }

//        if (reportSingleQueryResult.getCode() != 202 && reportSingleQueryResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportSingleQueryResultService.createSingleQueryResult(reportSingleQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.DELETE.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.DELETE.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-execute-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createExecuteSingleQueryResult(@RequestBody @Validated @NotNull final ReportSingleQueryResult reportSingleQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SQR.EXEC.REQ: " + reportSingleQueryResult);
        }

//        if (reportSingleQueryResult.getCode() != 201 && reportSingleQueryResult.getCode() != 406) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportSingleQueryResultService.createSingleQueryResult(reportSingleQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.EXEC.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.EXEC.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-get-single-query-by-id-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGetSingleQueryResult(@RequestBody @Validated @NotNull final ReportSingleQueryResult reportSingleQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SQR.GET.REQ: " + reportSingleQueryResult);
        }

//        if (reportSingleQueryResult.getCode() != 200 && reportSingleQueryResult.getCode() != 500) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportSingleQueryResultService.createSingleQueryResult(reportSingleQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.GET.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.GET.RESP: " + response);
            }
            return response;
        }
    }

    @RequestMapping(value = "/add-get-all-single-queries-result", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGetAllSingleQueriesResult(@RequestBody @Validated @NotNull final ReportSingleQueryResult reportSingleQueryResult) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SQR.ALL.REQ: " + reportSingleQueryResult);
        }

//        if (reportSingleQueryResult.getCode() != 200) {
//            throw new IllegalArgumentException("Unsupported code");
//        }

        try {
            reportSingleQueryResultService.createSingleQueryResult(reportSingleQueryResult);
            final ResponseEntity<Object> response = ResponseEntity.accepted().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.ALL.RESP: " + response);
            }
            return response;
        } catch (Exception ex) {
            final ResponseEntity<Object> response = ResponseEntity.badRequest().build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQR.ALL.RESP: " + response);
            }
            return response;
        }
    }

    //endregion CHECKER
}
