package com.example.productService.mapper;



import com.example.productService.dto.CategoryDTO;
import com.example.productService.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
