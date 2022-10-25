package com.example.demo.model.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public final class ReportBodyTableResult {

    private Integer resultId;
    private Integer code;
    private ReportTableDto table;

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

    public ReportTableDto getTable() {
        return table;
    }

    public void setTable(ReportTableDto table) {
        this.table = table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportBodyTableResult that = (ReportBodyTableResult) o;
        return Objects.equals(resultId, that.resultId) && Objects.equals(code, that.code) && Objects.equals(table, that.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, code, table);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("resultId", resultId)
            .append("code", code)
            .append("table", table)
            .toString();
    }
}
