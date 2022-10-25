package com.example.demo.model.dto;

import com.example.demo.model.entity.ReportTableColumn;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public final class ReportTableColumnDto {

    private String title;
    private String type;

    public ReportTableColumnDto() {
    }

    public ReportTableColumnDto(String title, String type) {
        this.title = title;
        this.type = type;
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
        final ReportTableColumnDto that = (ReportTableColumnDto) o;
        return Objects.equals(title, that.title)
            && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, type);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("title", title)
            .append("type", type)
            .toString();
    }

    public static ReportTableColumnDto build(final ReportTableColumn entity) {
        final ReportTableColumnDto dto = new ReportTableColumnDto();
        //dto.setTitle(entity.getTitle().toUpperCase());
        //dto.setType(DataTypesUtil.sqlTypeStandardize(entity.getType()));
        dto.setTitle(entity.getTitle());
        dto.setType(entity.getType());
        return dto;
    }
}
