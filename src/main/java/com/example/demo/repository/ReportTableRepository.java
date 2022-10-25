package com.example.demo.repository;

import com.example.demo.model.entity.ReportTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportTableRepository extends CrudRepository<ReportTable, String> {
    Optional<ReportTable> findByTableNameIgnoreCase(final String tableName);

    int deleteByTableNameIgnoreCase(final String tableName);
}
