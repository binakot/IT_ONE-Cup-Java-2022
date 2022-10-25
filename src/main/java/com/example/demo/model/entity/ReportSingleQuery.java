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
@Table(name = "report_single_queries")
public class ReportSingleQuery {

    @Id
    private Integer queryId;
    @Column(nullable = false)
    private String query;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "report_single_query_result_id")
    private ReportSingleQueryResult result;

    public ReportSingleQuery() {
    }

    public ReportSingleQuery(Integer queryId, String query) {
        this.queryId = queryId;
        this.query = query;
    }

    public ReportSingleQuery(Integer queryId, String query, ReportSingleQueryResult result) {
        this.queryId = queryId;
        this.query = query;
        this.result = result;
    }

    public Integer getQueryId() {
        return queryId;
    }

    public void setQueryId(final Integer queryId) {
        this.queryId = queryId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public ReportSingleQueryResult getResult() {
        return result;
    }

    public void setResult(final ReportSingleQueryResult result) {
        this.result = result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportSingleQuery that = (ReportSingleQuery) o;
        return Objects.equals(queryId, that.queryId)
            && Objects.equals(query, that.query)
            && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, query, result);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("queryId", queryId)
            .append("query", query)
            .append("result", result)
            .toString();
    }
}
