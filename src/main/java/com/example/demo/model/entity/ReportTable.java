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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "report_tables")
public class ReportTable {

    @Id
    private String tableName;
    @Column
    private Integer columnsAmount;
    @Column
    private String primaryKey;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_table_name")
    private List<ReportTableColumn> columnInfos = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "report_table_result_id")
    private ReportTableResult result;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public Integer getColumnsAmount() {
        return columnsAmount;
    }

    public void setColumnsAmount(final Integer columnsAmount) {
        this.columnsAmount = columnsAmount;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(final String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ReportTableColumn> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(final List<ReportTableColumn> columnInfos) {
        this.columnInfos = columnInfos;
    }

    public ReportTableResult getResult() {
        return result;
    }

    public void setResult(ReportTableResult result) {
        this.result = result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportTable that = (ReportTable) o;
        return Objects.equals(tableName, that.tableName)
            && Objects.equals(columnsAmount, that.columnsAmount)
            && Objects.equals(primaryKey, that.primaryKey)
            && Objects.equals(columnInfos, that.columnInfos)
            && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, columnsAmount, primaryKey, columnInfos, result);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("tableName", tableName)
            .append("columnsAmount", columnsAmount)
            .append("primaryKey", primaryKey)
            .append("columnInfos", columnInfos)
            .append("result", result)
            .toString();
    }
}
