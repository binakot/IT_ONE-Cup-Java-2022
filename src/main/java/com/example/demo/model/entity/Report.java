package com.example.demo.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    private Integer reportId;
    @Column
    private Integer tableAmount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "tables_reports",
        joinColumns = {@JoinColumn(name = "table_name")},
        inverseJoinColumns = {@JoinColumn(name = "report_id")}
    )
    private List<ReportTable> tables = new ArrayList<>();

    public Report() {
    }

    public Report(Integer reportId, Integer tableAmount, List<ReportTable> tables) {
        this.reportId = reportId;
        this.tableAmount = tableAmount;
        this.tables = tables;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getTableAmount() {
        return tableAmount;
    }

    public void setTableAmount(final Integer tableAmount) {
        this.tableAmount = tableAmount;
    }

    public List<ReportTable> getTables() {
        return tables;
    }

    public void setTables(final List<ReportTable> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Report report = (Report) o;
        return Objects.equals(reportId, report.reportId)
            && Objects.equals(tableAmount, report.tableAmount)
            && Objects.equals(tables, report.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, tableAmount, tables);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("reportId", reportId)
            .append("tableAmount", tableAmount)
            .append("tables", tables)
            .toString();
    }
}
