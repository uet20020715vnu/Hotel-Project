package com.example.common.entity.Base;



import java.time.LocalDateTime;

public abstract class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String createdBy;
    private String updatedBy;
    private String deletedBy;
}
