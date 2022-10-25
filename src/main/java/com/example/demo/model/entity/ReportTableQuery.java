package com.example.demo.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "report_table_queries")
public class ReportTableQuery {

    @Id
    private Integer queryId;
    @Column(nullable = false)
    private String tableName;
    @Column(nullable = false)
    private String query;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "report_table_query_result_id")
    private ReportTableQueryResult result;

    public ReportTableQuery() {
    }

    public ReportTableQuery(Integer queryId, String tableName, String query) {
        this.queryId = queryId;
        this.tableName = tableName;
        this.query = query;
    }

    public ReportTableQuery(Integer queryId, String tableName, String query, ReportTableQueryResult result) {
        this.queryId = queryId;
        this.tableName = tableName;
        this.query = query;
        this.result = result;
    }

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

    public ReportTableQueryResult getResult() {
        return result;
    }

    public void setResult(ReportTableQueryResult result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportTableQuery that = (ReportTableQuery) o;
        return Objects.equals(queryId, that.queryId)
            && Objects.equals(tableName, that.tableName)
            && Objects.equals(query, that.query)
            && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, tableName, query, result);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("queryId", queryId)
            .append("tableName", tableName)
            .append("query", query)
            .append("result", result)
            .toString();
    }
}
