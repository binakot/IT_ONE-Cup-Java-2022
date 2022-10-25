package com.example.demo.service;

import com.example.demo.model.dto.ReportCreateDto;
import com.example.demo.model.dto.ReportReadDto;
import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.ReportTable;
import com.example.demo.model.entity.ReportTableColumn;
import com.example.demo.repository.ReportRepository;
import com.example.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportTableService reportTableService;

    @Autowired
    public ReportService(final ReportRepository reportRepository,
                         final ReportTableService reportTableService) {
        this.reportRepository = reportRepository;
        this.reportTableService = reportTableService;
    }

    public Optional<ReportReadDto> getByReportId(final Integer reportId) {
        final Optional<Report> report = reportRepository.findById(reportId);
        if (report.isEmpty()) {
            throw new IllegalArgumentException("Report is not exists: " + reportId);
        }

        return report.map(this::calculateReport);
    }

    public Report createReport(final ReportCreateDto reportCreateDto) {
        if (reportRepository.findById(reportCreateDto.getReportId()).isPresent()) {
            throw new IllegalArgumentException("Report already exists: " + reportCreateDto.getReportId());
        }

        if (reportCreateDto.getTableAmount() != reportCreateDto.getTables().size()) {
            throw new IllegalArgumentException("Tables amount is not equals tables size");
        }

        for (ReportCreateDto.ReportCreateTableDto tableDto : reportCreateDto.getTables()) {
            final Optional<ReportTable> storedTable = reportTableService.getByTableName(tableDto.getTableName());
            if (storedTable.isEmpty()) {
                throw new IllegalArgumentException("Report table is not exist: " + tableDto.getTableName());
            }

            for (ReportCreateDto.ReportCreateTableColumnDto columnDto : tableDto.getColumns()) {
                if (!ValidationUtil.validateReportTableColumnTitle(columnDto.getTitle())) {
                    throw new IllegalArgumentException("Invalid report table column title: " + columnDto.getTitle());
                }
                if (!ValidationUtil.validateReportTableColumnType(columnDto.getType())) {
                    throw new IllegalArgumentException("Invalid report table column type: " + columnDto.getType());
                }
                if (storedTable.get().getColumnInfos().stream().map(ReportTableColumn::getTitle).noneMatch(t -> t.equalsIgnoreCase(columnDto.getTitle()))) {
                    throw new IllegalArgumentException("Report table column is not exists: " + columnDto.getTitle());
                }
            }
        }

        final Report result = new Report();
        result.setReportId(reportCreateDto.getReportId());
        result.setTableAmount(reportCreateDto.getTableAmount());
        result.setTables(reportCreateDto.getTables().stream()
            .map(tableDto ->
                reportTableService.getByTableName(tableDto.getTableName())
                    .orElseThrow(() -> new IllegalArgumentException("Missing report table: " + tableDto.getTableName()))
            ).collect(Collectors.toList()));

        return reportRepository.save(result);
    }

    private ReportReadDto calculateReport(final Report report) {
        final ReportReadDto result = new ReportReadDto();
        result.setReportId(report.getReportId());
        result.setTableAmount(report.getTableAmount());
        result.setTables(report.getTables().stream()
            .map(ReportReadDto.ReportReadTableDto::build)
            .collect(Collectors.toList()));

        result.getTables().forEach(t -> {
            final Map<String, Integer> tableCounters = reportTableService.countNotNullValuesByTable(t.getTableName());
            t.getColumns().forEach(c -> {
                final Integer columnCount = tableCounters.get(c.getTitle().toUpperCase());
                if (columnCount != null) {
                    c.setSize(columnCount);
                }
            });
            t.setColumns(t.getColumns().stream()
                .filter(c -> c.getSize() != null)
                .collect(Collectors.toList()));
        });

        return result;
    }
}
