package com.example.productService.dto;

import jakarta.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private String name;
    @Nullable()
    private Integer parent_id;
    private Integer id;
    public CategoryDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
