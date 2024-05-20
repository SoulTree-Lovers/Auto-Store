package com.example.memorydb.ali.db.repository;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AliExpressRepository extends JpaRepository<ProductEntityFromAli, Long> {
    //List<ProductEntityFromAli> findByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<ProductEntityFromAli> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}