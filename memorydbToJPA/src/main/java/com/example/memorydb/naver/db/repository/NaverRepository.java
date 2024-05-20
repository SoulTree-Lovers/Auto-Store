package com.example.memorydb.naver.db.repository;

import com.example.memorydb.naver.db.entity.NaverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverRepository extends JpaRepository<NaverEntity, Long> {
}
