package com.example.demo.model.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;

public final class ReportBodyListTableQueryResult {

    private Integer resultId;
    private Integer code;
    private List<ReportTableQueryDto> tableQueries;

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

    public List<ReportTableQueryDto> getTableQueries() {
        return tableQueries;
    }

    public void setTableQueries(List<ReportTableQueryDto> tableQueries) {
        this.tableQueries = tableQueries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportBodyListTableQueryResult that = (ReportBodyListTableQueryResult) o;
        return Objects.equals(resultId, that.resultId) && Objects.equals(code, that.code) && Objects.equals(tableQueries, that.tableQueries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, code, tableQueries);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("resultId", resultId)
            .append("code", code)
            .append("tableQueries", tableQueries)
            .toString();
    }
}
