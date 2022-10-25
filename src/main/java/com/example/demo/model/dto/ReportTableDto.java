package com.example.demo.model.dto;

import com.example.demo.model.entity.ReportTable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ReportTableDto {

    private String tableName;
    private Integer columnsAmount;
    private String primaryKey;
    private List<ReportTableColumnDto> columnInfos;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public Integer getColumnsAmount() {
        return columnsAmount;
    }

    public void setColumnsAmount(final Integer columnsAmount) {
        this.columnsAmount = columnsAmount;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(final String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ReportTableColumnDto> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(final List<ReportTableColumnDto> columnInfos) {
        this.columnInfos = columnInfos;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportTableDto that = (ReportTableDto) o;
        return Objects.equals(tableName, that.tableName)
            && Objects.equals(columnsAmount, that.columnsAmount)
            && Objects.equals(primaryKey, that.primaryKey)
            && Objects.equals(columnInfos, that.columnInfos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, columnsAmount, primaryKey, columnInfos);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("tableName", tableName)
            .append("columnsAmount", columnsAmount)
            .append("primaryKey", primaryKey)
            .append("columnInfos", columnInfos)
            .toString();
    }

    public static ReportTableDto build(final ReportTable entity) {
        final ReportTableDto dto = new ReportTableDto();
        dto.setTableName(entity.getTableName());
        dto.setColumnsAmount(entity.getColumnsAmount());
        dto.setPrimaryKey(entity.getPrimaryKey().toLowerCase());
        if (entity.getColumnInfos() != null) {
            dto.setColumnInfos(entity.getColumnInfos().stream().map(ReportTableColumnDto::build).collect(Collectors.toList()));
        }
        return dto;
    }
}
