package com.example.productService.controller;


import com.example.productService.dto.CategoryDTO;
import com.example.productService.entity.Category;
import com.example.productService.exception.CategoryNotFoundException;
import com.example.productService.repository.CategoryRepository;
import com.example.productService.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/allcategory")
    public ResponseEntity<Page<CategoryDTO>> getAllCategory(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit){
        Page<CategoryDTO> categoryDTOS = categoryService.getAllCategory(PageRequest.of(page -1, limit));
        return ResponseEntity.ok(categoryDTOS);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer id) {
            CategoryDTO category = categoryService.getCategoryById(id);
            if (category == null) {
                throw new CategoryNotFoundException("Category not found with id: " + id);
            }
            return ResponseEntity.ok(category);
        }

    @GetMapping("/name/{name}")
    public List<CategoryDTO> getCategoryByName(@PathVariable String name) {
        List<CategoryDTO> category = categoryService.getCategoryByName(name);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found with name: " + name);
        }
        return category;
    }

    @PostMapping("/")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id ,@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if(result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(),fieldError -> fieldError.getDefaultMessage()));
        return  new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        categoryService.moveToTrash(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/trash")
    public ResponseEntity<?> getInTrashCategory(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit){
        return ResponseEntity.ok(categoryService.getInTrash(PageRequest.of(page -1, limit)));
    }
}
