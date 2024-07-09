package com.example.productService.entity.Base;



import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String createdBy;
    private String updatedBy;
    private String deletedBy;
}
