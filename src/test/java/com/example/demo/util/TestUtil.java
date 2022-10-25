package com.example.demo.util;

import com.example.demo.model.entity.ReportResult;
import com.example.demo.model.entity.ReportSingleQuery;
import com.example.demo.model.entity.ReportSingleQueryResult;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import com.example.demo.model.entity.ReportTableQuery;
import com.example.demo.model.entity.ReportTableQueryResult;
import com.example.demo.model.entity.ReportTableResult;
import org.apache.commons.io.IOUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Objects;

public enum TestUtil {;

    public static ReportTable buildTestTable() {
        final ReportTable result = new ReportTable();
        result.setTableName("Artists");
        result.setColumnsAmount(3);
        result.setPrimaryKey("id");
        result.setColumnInfos(List.of(
            buildTestTableColumn("id", "int4"),
            buildTestTableColumn("name", "varchar"),
            buildTestTableColumn("age", "int4")
        ));
        return result;
    }

    public static ReportTableColumn buildTestTableColumn(final String title, final String type) {
        final ReportTableColumn result = new ReportTableColumn();
        result.setTitle(title);
        result.setType(type);
        return result;
    }

    public static ReportTableResult buildTestTableResult() {
        final ReportTableResult result = new ReportTableResult();
        result.setResultId(1);
        result.setCode(200);
        return result;
    }


    public static ReportTableQuery buildTestTableQuery() {
        final ReportTableQuery result = new ReportTableQuery();
        result.setQueryId(1);
        result.setTableName("Artists");
        result.setQuery("SELECT * FROM Artists");
        return result;
    }

    public static ReportTableQueryResult buildTestTableQueryResult() {
        final ReportTableQueryResult result = new ReportTableQueryResult();
        result.setResultId(1);
        result.setCode(200);
        return result;
    }


    public static ReportSingleQuery buildTestSingleQuery() {
        final ReportSingleQuery result = new ReportSingleQuery();
        result.setQueryId(1);
        result.setQuery("SELECT * FROM Artists ORDER BY id DESC LIMIT 1");
        return result;
    }

    public static ReportSingleQueryResult buildTestSingleQueryResult() {
        final ReportSingleQueryResult result = new ReportSingleQueryResult();
        result.setResultId(1);
        result.setCode(200);
        return result;
    }


    public static ReportResult buildTestReportResult() {
        final ReportResult result = new ReportResult();
        result.setResultId(1);
        result.setCode(200);
        return result;
    }


    public static String buildSqlTableInfo(final String tableName, final DataSource dataSource) {
        try (final PreparedStatement stmt = dataSource.getConnection().prepareStatement("SELECT * FROM " + tableName + " LIMIT 1")) {
            final ResultSetMetaData rsmd = stmt.getMetaData();

            final StringBuilder sb = new StringBuilder();
            sb.append(tableName + "[");

            final int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                final String columnName = rsmd.getColumnName(i);
                final String columnType = rsmd.getColumnTypeName(i);
                sb.append(columnName + ":" + columnType + ";");
            }
            sb.append("]");

            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(tableName + ": " + ex.getMessage(), ex);
        }
    }

    public static String readFileAsString(final String fileName) throws IOException {
        return IOUtils.toString(Objects.requireNonNull(TestUtil.class.getClassLoader().getResource(fileName)), StandardCharsets.UTF_8);
    }
}
