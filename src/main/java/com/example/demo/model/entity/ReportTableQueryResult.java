package com.example.demo.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "report_table_query_results")
public class ReportTableQueryResult {

    @Id
    private Integer resultId;
    @Column(nullable = false)
    private Integer code;

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(final Integer resultId) {
        this.resultId = resultId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportTableQueryResult that = (ReportTableQueryResult) o;
        return Objects.equals(resultId, that.resultId)
            && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, code);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("resultId", resultId)
            .append("code", code)
            .toString();
    }
}
