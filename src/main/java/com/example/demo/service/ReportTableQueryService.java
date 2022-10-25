package com.example.demo.service;

import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.repository.ReportTableQueryRepository;
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
public class ReportTableQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTableQueryService.class);

    private final EntityManager entityManager;
    private final ReportTableQueryRepository reportTableQueryRepository;

    @Autowired
    public ReportTableQueryService(final EntityManager entityManager,
                                   final ReportTableQueryRepository reportTableQueryRepository) {
        this.entityManager = entityManager;
        this.reportTableQueryRepository = reportTableQueryRepository;
    }

    public ReportTableQuery createTableQuery(final ReportTableQuery reportTableQuery) {
        if (reportTableQuery.getTableName().length() > 50) {
            throw new IllegalArgumentException("Table name too large");
        }
        if (reportTableQuery.getQuery().length() > 120) {
            throw new IllegalArgumentException("Query too large");
        }
        return reportTableQueryRepository.save(reportTableQuery);
    }

    public void deleteByQueryId(final Integer queryId) {
        reportTableQueryRepository.deleteById(queryId);
    }

    public Optional<ReportTableQuery> getByQueryId(final Integer queryId) {
        return reportTableQueryRepository.findById(queryId);
    }

    public List<ReportTableQuery> getAll() {
        return StreamSupport.stream(reportTableQueryRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<ReportTableQuery> getAllByTableName(final String tableName) {
        return reportTableQueryRepository.findAllByTableName(tableName);
    }

    public void execute(final ReportTableQuery reportTableQuery) {
        try {
            final String sqlQuery = reportTableQuery.getQuery();
            LOGGER.debug("TQ.EXEC.SQL: " + sqlQuery);

            final Query nativeQuery = entityManager.createNativeQuery(sqlQuery);
            if (sqlQuery.toUpperCase().startsWith("SELECT")) {
                nativeQuery.getResultList();
            } else {
                nativeQuery.executeUpdate();
            }
        } catch (Exception ex) {
            LOGGER.error("TQ.EXEC.SQL.ERR: " + ex.getMessage());
            throw new IllegalArgumentException("SQL ERROR");
        }
    }
}
