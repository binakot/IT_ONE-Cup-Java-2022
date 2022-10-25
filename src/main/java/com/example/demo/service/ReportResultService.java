package com.example.demo.service;

import com.example.demo.model.entity.ReportResult;
import com.example.demo.repository.ReportResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ReportResultService {

    private final ReportResultRepository reportResultRepository;

    @Autowired
    public ReportResultService(final ReportResultRepository reportResultRepository) {
        this.reportResultRepository = reportResultRepository;
    }

    public ReportResult createReportResult(final ReportResult reportResult) {
        return reportResultRepository.save(reportResult);
    }

    public Optional<ReportResult> getReportResultByResultId(final Integer resultId) {
        return reportResultRepository.findById(resultId);
    }
}
