package com.example.memorydb.entity;

import com.example.memorydb.entity.PrimaryKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public abstract class Entity implements PrimaryKey {

    @Getter
    @Setter
    private Long id;
}
