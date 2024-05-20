package com.example.memorydb.naver.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product_no")
@Data
@Table(name = "product_no")
@EntityListeners(AuditingEntityListener.class)
public class NaverEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_product_no", columnDefinition = "TEXT")
    private String originProductNo;
}
