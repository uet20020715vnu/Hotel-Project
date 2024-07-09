package com.example.productService.service;


import com.example.productService.dto.CategoryDTO;
import com.example.productService.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Page<CategoryDTO> getAllCategory(Pageable pageable);
    CategoryDTO getCategoryById(Integer id);
    void addCategory(CategoryDTO categoryDTO);
    void updateCategory(Integer id, CategoryDTO categoryDTO);
    void deleteCategory(Integer id);
    Category convertToEntityId(Integer id, CategoryDTO categoryDTO);
    List<CategoryDTO> getCategoryByName(String name);
    void moveToTrash(Integer id);
    Page<CategoryDTO> getInTrash(Pageable pageable);
}
