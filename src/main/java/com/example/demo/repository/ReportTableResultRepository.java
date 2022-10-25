package com.example.demo.repository;

import com.example.demo.model.entity.ReportTableResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTableResultRepository extends CrudRepository<ReportTableResult, Integer> {
}
