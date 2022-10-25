package com.example.demo.service;

import com.example.demo.model.entity.ReportTableQueryResult;
import com.example.demo.repository.ReportTableQueryResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ReportTableQueryResultService {

    private final ReportTableQueryResultRepository reportTableQueryResultRepository;

    @Autowired
    public ReportTableQueryResultService(final ReportTableQueryResultRepository reportTableQueryResultRepository) {
        this.reportTableQueryResultRepository = reportTableQueryResultRepository;
    }

    public ReportTableQueryResult createTableQueryResult(final ReportTableQueryResult reportTableQueryResult) {
        return reportTableQueryResultRepository.save(reportTableQueryResult);
    }

    public Optional<ReportTableQueryResult> getTableQueryResultByResultId(final Integer resultId) {
        return reportTableQueryResultRepository.findById(resultId);
    }
}
