package com.example.demo.service;

import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.repository.ReportSingleQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ReportSingleQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportSingleQueryService.class);

    private final EntityManager entityManager;
    private final ReportSingleQueryRepository reportSingleQueryRepository;
    private final ReportTableService reportTableService;

    @Autowired
    public ReportSingleQueryService(final EntityManager entityManager,
                                    final ReportSingleQueryRepository reportSingleQueryRepository,
                                    final ReportTableService reportTableService) {
        this.entityManager = entityManager;
        this.reportSingleQueryRepository = reportSingleQueryRepository;
        this.reportTableService = reportTableService;
    }

    public ReportSingleQuery createSingleQuery(final ReportSingleQuery reportSingleQuery) {
        if (reportSingleQuery.getQuery().length() > 120) {
            throw new IllegalArgumentException("Query too large");
        }
        return reportSingleQueryRepository.save(reportSingleQuery);
    }

    public void deleteByQueryId(final Integer queryId) {
        reportSingleQueryRepository.deleteById(queryId);
    }

    public Optional<ReportSingleQuery> getByQueryId(final Integer queryId) {
        return reportSingleQueryRepository.findById(queryId);
    }

    public List<ReportSingleQuery> getAll() {
        return StreamSupport.stream(reportSingleQueryRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public void execute(final ReportSingleQuery reportSingleQuery) {
        try {
            final String sqlQuery = reportSingleQuery.getQuery().replaceAll(";", "").strip();
            LOGGER.debug("SQ.EXEC.SQL: " + sqlQuery);

            final Query nativeQuery = entityManager.createNativeQuery(sqlQuery);
            if (sqlQuery.toUpperCase().startsWith("SELECT")) {
                nativeQuery.getResultList();
            } else {
                final String[] sqlWords = sqlQuery.split(" ", -1);

                if (sqlQuery.toUpperCase().matches("^ALTER TABLE \\w+ RENAME TO \\w+")) {
                    reportTableService.renameTable(sqlWords[2], sqlWords[5]); // ALTER TABLE V1 RENAME TO V2
                    nativeQuery.executeUpdate();
                } else if (sqlQuery.toUpperCase().matches("^DROP TABLE \\w+")) {
                    reportTableService.deleteByTableName(sqlWords[2]); // DROP TABLE V1
                } else {
                    nativeQuery.executeUpdate();
                }
            }
        } catch (Exception ex) {
            LOGGER.error("SQ.EXEC.SQL.ERR: " + ex.getMessage());
            throw new IllegalArgumentException("SQL ERROR");
        }
    }
}
