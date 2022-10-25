package com.example.demo.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "report_table_columns")
public class ReportTableColumn {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String type;

    public ReportTableColumn() {
    }

    public ReportTableColumn(final String title, final String type) {
        this.title = title;
        this.type = type;
    }

    public ReportTableColumn(final Integer id, final String title, final String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportTableColumn that = (ReportTableColumn) o;
        return Objects.equals(title, that.title)
            && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("title", title)
            .append("type", type)
            .toString();
    }
}
