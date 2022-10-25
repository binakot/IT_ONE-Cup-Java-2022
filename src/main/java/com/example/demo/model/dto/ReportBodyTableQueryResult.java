package com.example.demo.model.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public final class ReportBodyTableQueryResult {

    private Integer resultId;
    private Integer code;
    private ReportTableQueryDto tableQuery;

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

    public ReportTableQueryDto getTableQuery() {
        return tableQuery;
    }

    public void setTableQuery(ReportTableQueryDto tableQuery) {
        this.tableQuery = tableQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportBodyTableQueryResult that = (ReportBodyTableQueryResult) o;
        return Objects.equals(resultId, that.resultId) && Objects.equals(code, that.code) && Objects.equals(tableQuery, that.tableQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, code, tableQuery);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("resultId", resultId)
            .append("code", code)
            .append("tableQuery", tableQuery)
            .toString();
    }
}
