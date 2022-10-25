package com.example.demo.repository;

import com.example.demo.model.entity.ReportResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportResultRepository extends CrudRepository<ReportResult, Integer> {
}
