package com.example.demo.service;

import com.example.demo.model.entity.ReportTableResult;
import com.example.demo.repository.ReportTableResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ReportTableResultService {

    private final ReportTableResultRepository reportTableResultRepository;

    @Autowired
    public ReportTableResultService(final ReportTableResultRepository reportTableResultRepository) {
        this.reportTableResultRepository = reportTableResultRepository;
    }

    public ReportTableResult createTableResult(final ReportTableResult reportTableResult) {
        return reportTableResultRepository.save(reportTableResult);
    }

    public Optional<ReportTableResult> getTableResultByResultId(final Integer resultId) {
        return reportTableResultRepository.findById(resultId);
    }
}
