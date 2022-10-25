package com.example.demo.model.dto;

import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ReportCreateDto {

    private Integer reportId;
    private Integer tableAmount;
    private List<ReportCreateTableDto> tables;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getTableAmount() {
        return tableAmount;
    }

    public void setTableAmount(final Integer tableAmount) {
        this.tableAmount = tableAmount;
    }

    public List<ReportCreateTableDto> getTables() {
        return tables;
    }

    public void setTables(final List<ReportCreateTableDto> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportCreateDto that = (ReportCreateDto) o;
        return Objects.equals(reportId, that.reportId)
            && Objects.equals(tableAmount, that.tableAmount)
            && Objects.equals(tables, that.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, tableAmount, tables);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("reportId", reportId)
            .append("tableAmount", tableAmount)
            .append("tables", tables)
            .toString();
    }

    public static ReportCreateDto build(final Report entity) {
        final ReportCreateDto dto = new ReportCreateDto();
        dto.setReportId(entity.getReportId());
        dto.setTableAmount(entity.getTableAmount());
        dto.setTables(entity.getTables().stream().map(ReportCreateDto.ReportCreateTableDto::build).collect(Collectors.toList()));
        return dto;
    }

    public static final class ReportCreateTableDto {

        private String tableName;
        private List<ReportCreateTableColumnDto> columns;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(final String tableName) {
            this.tableName = tableName;
        }

        public List<ReportCreateTableColumnDto> getColumns() {
            return columns;
        }

        public void setColumns(final List<ReportCreateTableColumnDto> columns) {
            this.columns = columns;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ReportCreateTableDto that = (ReportCreateTableDto) o;
            return Objects.equals(tableName, that.tableName)
                && Objects.equals(columns, that.columns);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tableName, columns);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("tableName", tableName)
                .append("columns", columns)
                .toString();
        }

        public static ReportCreateTableDto build(final ReportTable entity) {
            final ReportCreateTableDto dto = new ReportCreateTableDto();
            dto.setTableName(entity.getTableName());
            dto.setColumns(entity.getColumnInfos().stream().map(ReportCreateTableColumnDto::build).collect(Collectors.toList()));
            return dto;
        }
    }

    public static final class ReportCreateTableColumnDto {

        private String title;
        private String type;

        public ReportCreateTableColumnDto() {
        }

        public ReportCreateTableColumnDto(String title, String type) {
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ReportCreateTableColumnDto that = (ReportCreateTableColumnDto) o;
            return Objects.equals(title, that.title)
                && Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, type);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("title", title)
                .append("type", type)
                .toString();
        }

        public static ReportCreateTableColumnDto build(final ReportTableColumn entity) {
            final ReportCreateTableColumnDto dto = new ReportCreateTableColumnDto();
            dto.setTitle(entity.getTitle());
            dto.setType(entity.getType());
            return dto;
        }
    }
}
