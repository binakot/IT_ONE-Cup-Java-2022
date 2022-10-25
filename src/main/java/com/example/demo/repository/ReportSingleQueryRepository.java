package com.example.demo.repository;

import com.example.demo.model.entity.ReportSingleQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSingleQueryRepository extends CrudRepository<ReportSingleQuery, Integer> {
}
