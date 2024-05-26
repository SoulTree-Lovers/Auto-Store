package com.example.memorydb.ali.db.repository;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AliExpressRepository extends JpaRepository<ProductEntityFromAli, Long> {
    //List<ProductEntityFromAli> findByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<ProductEntityFromAli> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p.createdAt FROM products p ORDER BY p.createdAt ASC")
    List<LocalDateTime> findOldestDates();

    List<ProductEntityFromAli> findByCreatedAt(LocalDateTime createdAt);
}