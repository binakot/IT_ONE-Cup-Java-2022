package com.example.demo.model.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public final class ReportBodyResult {

    private Integer resultId;
    private Integer code;
    private ReportReadDto getReport;

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

    public ReportReadDto getGetReport() {
        return getReport;
    }

    public void setGetReport(ReportReadDto getReport) {
        this.getReport = getReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportBodyResult that = (ReportBodyResult) o;
        return Objects.equals(resultId, that.resultId) && Objects.equals(code, that.code) && Objects.equals(getReport, that.getReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, code, getReport);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("resultId", resultId)
            .append("code", code)
            .append("getReport", getReport)
            .toString();
    }
}
