package com.example.demo.service;

import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import com.example.demo.repository.ReportTableRepository;
import com.example.demo.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ReportTableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTableService.class);

    private final DataSource dataSource;
    private final EntityManager entityManager;
    private final ReportTableRepository reportTableRepository;
    private final ReportTableQueryService reportTableQueryService;

    @Autowired
    public ReportTableService(final DataSource dataSource,
                              final EntityManager entityManager,
                              final ReportTableRepository reportTableRepository,
                              final ReportTableQueryService reportTableQueryService) {
        this.dataSource = dataSource;
        this.entityManager = entityManager;
        this.reportTableRepository = reportTableRepository;
        this.reportTableQueryService = reportTableQueryService;
    }

    public ReportTable createTable(final ReportTable reportTable) {
        if (reportTableRepository.findByTableNameIgnoreCase(reportTable.getTableName()).isPresent()) {
            throw new IllegalArgumentException("Table already exists: " + reportTable.getTableName());
        }

        ValidationUtil.validateTable(reportTable);

        final ReportTable createdTable = reportTableRepository.save(reportTable);
        try {
            final String sqlQuery = buildCreateSqlQuery(createdTable);
            LOGGER.debug("T.CREATE.SQL: " + sqlQuery);
            entityManager.createNativeQuery(sqlQuery).executeUpdate();
        } catch (Exception ex) {
            LOGGER.error("T.CREATE.SQL.ERR: " + ex.getMessage());
            throw ex;
        }

        return createdTable;
    }

    public Optional<ReportTable> getByTableName(final String tableName) {
        return tableName.length() > 50
            ? Optional.empty()
            : reportTableRepository.findByTableNameIgnoreCase(tableName);
    }

    public void deleteByTableName(final String tableName) {
        try {
            final String sqlQuery = buildDeleteSqlQuery(tableName);
            LOGGER.debug("T.DELETE.SQL: " + sqlQuery);
            entityManager.createNativeQuery(sqlQuery).executeUpdate();
        } catch (Exception ex) {
            LOGGER.error("T.DELETE.SQL.ERR: " + ex.getMessage());
            throw ex;
        }
        reportTableRepository.deleteByTableNameIgnoreCase(tableName);
        reportTableQueryService.getAllByTableName(tableName)
            .forEach(q -> reportTableQueryService.deleteByQueryId(q.getQueryId()));
    }

    public void renameTable(final String origin, final String updated) {
        // NOP
    }

    public Map<String, Integer> countNotNullValuesByTable(final String tableName) {
        final Map<String, Integer> result = new HashMap<>();

        try (final Statement stmt = dataSource.getConnection().createStatement();
             final ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            final ResultSetMetaData rsmd = rs.getMetaData();
            final int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                final String columnName = rsmd.getColumnName(i);
                result.put(columnName, 0);
            }

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    final String columnName = rsmd.getColumnName(i);
                    final Object curValue = rs.getObject(i);
                    if (Objects.nonNull(curValue)) {
                        result.put(columnName, result.get(columnName) + 1);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return result;
    }

    private static String buildCreateSqlQuery(final ReportTable reportTable) {
        final String pkName = reportTable.getPrimaryKey();

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("CREATE TABLE ")
            .append(reportTable.getTableName()).append("(");

        for (int i = 0; i < reportTable.getColumnInfos().size(); i++) {
            final ReportTableColumn curColumn = reportTable.getColumnInfos().get(i);

            stringBuilder.append(curColumn.getTitle() + " " + curColumn.getType());

            if (curColumn.getTitle().equalsIgnoreCase(pkName)) {
                stringBuilder.append(" PRIMARY KEY");
            }

            if (i != reportTable.getColumnInfos().size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    private static String buildDeleteSqlQuery(final String tableName) {
        return "DROP TABLE " + tableName;
    }
}
