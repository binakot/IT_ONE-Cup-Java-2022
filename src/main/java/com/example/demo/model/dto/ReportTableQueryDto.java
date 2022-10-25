package com.example.demo.model.dto;

import com.example.demo.model.entity.ReportTableQuery;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public final class ReportTableQueryDto {

    private Integer queryId;
    private String tableName;
    private String query;

    public Integer getQueryId() {
        return queryId;
    }

    public void setQueryId(final Integer queryId) {
        this.queryId = queryId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportTableQueryDto that = (ReportTableQueryDto) o;
        return Objects.equals(queryId, that.queryId)
            && Objects.equals(tableName, that.tableName)
            && Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, tableName, query);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("queryId", queryId)
            .append("tableName", tableName)
            .append("query", query)
            .toString();
    }

    public static ReportTableQueryDto build(final ReportTableQuery entity) {
        final ReportTableQueryDto dto = new ReportTableQueryDto();
        dto.setQueryId(entity.getQueryId());
        dto.setTableName(entity.getTableName());
        dto.setQuery(entity.getQuery());
        return dto;
    }
}
