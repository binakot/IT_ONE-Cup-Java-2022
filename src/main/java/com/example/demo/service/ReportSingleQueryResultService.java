package com.example.demo.service;

import com.example.demo.model.entity.ReportSingleQueryResult;
import com.example.demo.repository.ReportSingleQueryResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ReportSingleQueryResultService {

    private final ReportSingleQueryResultRepository reportSingleQueryResultRepository;

    @Autowired
    public ReportSingleQueryResultService(final ReportSingleQueryResultRepository reportSingleQueryResultRepository) {
        this.reportSingleQueryResultRepository = reportSingleQueryResultRepository;
    }

    public ReportSingleQueryResult createSingleQueryResult(final ReportSingleQueryResult reportSingleQueryResult) {
        return reportSingleQueryResultRepository.save(reportSingleQueryResult);
    }

    public Optional<ReportSingleQueryResult> getSingleQueryResultByResultId(final Integer resultId) {
        return reportSingleQueryResultRepository.findById(resultId);
    }
}
