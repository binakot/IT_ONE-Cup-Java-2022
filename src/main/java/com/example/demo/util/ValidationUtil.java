package com.example.demo.util;

import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;

public enum ValidationUtil {;

    public static void validateTable(final ReportTable reportTable) {
        if (reportTable.getColumnsAmount() != reportTable.getColumnInfos().size()) {
            throw new IllegalArgumentException("Columns amount is not equals column infos size");
        }

        if (reportTable.getColumnInfos().stream().noneMatch(c -> reportTable.getPrimaryKey().equals(c.getTitle()))) {
            throw new IllegalArgumentException("Primary key column is not found");
        }

        if (!ValidationUtil.validateReportTableName(reportTable.getTableName())) {
            throw new IllegalArgumentException("Invalid table name: " + reportTable.getTableName());
        }

        for (ReportTableColumn columnInfo : reportTable.getColumnInfos()) {
            if (!ValidationUtil.validateReportTableColumnTitle(columnInfo.getTitle())) {
                throw new IllegalArgumentException("Invalid column title: " + columnInfo.getTitle());
            }
            if (!ValidationUtil.validateReportTableColumnType(columnInfo.getType())) {
                throw new IllegalArgumentException("Invalid column type: " + columnInfo.getType());
            }
        }
    }

    public static boolean validateReportTableName(final String tableName) {
        return tableName.matches("[A-Za-z0-9]+");
    }

    public static boolean validateReportTableColumnTitle(final String columnTitle) {
        return columnTitle.matches("[A-Za-z0-9]+");
    }

    public static boolean validateReportTableColumnType(final String columnType) {
        return columnType.matches("[A-Za-z0-9()]+");
    }
}
