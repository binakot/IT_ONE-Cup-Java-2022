package com.example.demo.model.dto;

import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ReportReadDto {

    private Integer reportId;
    private Integer tableAmount;
    private List<ReportReadDto.ReportReadTableDto> tables;

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

    public List<ReportReadDto.ReportReadTableDto> getTables() {
        return tables;
    }

    public void setTables(final List<ReportReadDto.ReportReadTableDto> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportReadDto that = (ReportReadDto) o;
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

    public static ReportReadDto build(final Report entity) {
        final ReportReadDto dto = new ReportReadDto();
        dto.setReportId(entity.getReportId());
        dto.setTableAmount(entity.getTableAmount());
        dto.setTables(entity.getTables().stream().map(ReportReadTableDto::build).collect(Collectors.toList()));
        return dto;
    }

    public static final class ReportReadTableDto {

        private String tableName;
        private List<ReportReadDto.ReportReadTableColumnDto> columns;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(final String tableName) {
            this.tableName = tableName;
        }

        public List<ReportReadDto.ReportReadTableColumnDto> getColumns() {
            return columns;
        }

        public void setColumns(final List<ReportReadDto.ReportReadTableColumnDto> columns) {
            this.columns = columns;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ReportReadDto.ReportReadTableDto that = (ReportReadDto.ReportReadTableDto) o;
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

        public static ReportReadDto.ReportReadTableDto build(final ReportTable entity) {
            final ReportReadDto.ReportReadTableDto dto = new ReportReadDto.ReportReadTableDto();
            dto.setTableName(entity.getTableName());
            dto.setColumns(entity.getColumnInfos().stream().map(ReportReadDto.ReportReadTableColumnDto::build).collect(Collectors.toList()));
            return dto;
        }
    }

    public static final class ReportReadTableColumnDto {

        private String title;
        private String type;
        private Integer size;

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

        public Integer getSize() {
            return size;
        }

        public void setSize(final Integer size) {
            this.size = size;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ReportReadTableColumnDto that = (ReportReadTableColumnDto) o;
            return Objects.equals(title, that.title)
                && Objects.equals(type, that.type)
                && Objects.equals(size, that.size);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, type, size);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("title", title)
                .append("type", type)
                .append("size", size)
                .toString();
        }

        public static ReportReadDto.ReportReadTableColumnDto build(final ReportTableColumn entity) {
            final ReportReadDto.ReportReadTableColumnDto dto = new ReportReadDto.ReportReadTableColumnDto();
            dto.setTitle(entity.getTitle());
            dto.setType(entity.getType());
            return dto;
        }
    }
}
