package com.example.demo.model.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public final class ReportBodySingleQueryResult {

    private Integer resultId;
    private Integer code;
    private Integer queryId;
    private String query;

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getQueryId() {
        return queryId;
    }

    public void setQueryId(Integer queryId) {
        this.queryId = queryId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportBodySingleQueryResult that = (ReportBodySingleQueryResult) o;
        return Objects.equals(resultId, that.resultId)
            && Objects.equals(code, that.code)
            && Objects.equals(queryId, that.queryId)
            && Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, code, queryId, query);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("resultId", resultId)
            .append("code", code)
            .append("queryId", queryId)
            .append("query", query)
            .toString();
    }
}
