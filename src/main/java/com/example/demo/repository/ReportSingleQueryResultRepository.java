package com.example.demo.repository;

import com.example.demo.model.entity.ReportSingleQueryResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSingleQueryResultRepository extends CrudRepository<ReportSingleQueryResult, Integer> {
}
