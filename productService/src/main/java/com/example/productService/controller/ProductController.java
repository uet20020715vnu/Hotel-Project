package com.example.productService.controller;


import com.example.productService.dto.ProductDTO;
import com.example.productService.entity.Category;
import com.example.productService.entity.Product;
import com.example.productService.exception.NotFoundException;
import com.example.productService.repository.CategoryRepository;
import com.example.productService.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    private final CategoryRepository categoryRepository;

    @GetMapping()
    public Page<ProductDTO> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(PageRequest.of(page -1, size));
    }

    @GetMapping("/name")
    public ResponseEntity<?> getProductByName(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int limit,
                                                       @RequestParam String name) {
        Page<ProductDTO> product = productService.getProductByName(PageRequest.of(page-1,limit), name);
        if (product == null) {
            throw new NotFoundException("Product not found with id: " + name);
        }
        return ResponseEntity.ok(product);
    }
    @GetMapping("Categories/{categoryId}")
    public Page<ProductDTO> findByCategory( @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int limit,
                                            @PathVariable Integer categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(!category.isPresent()){
            throw new RuntimeException("Can not find category with id " + categoryId);
        }
        return productService.findByCategory(PageRequest.of(page-1, limit),category.get());
    }
    @PostMapping()
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        productService.addProduct(productDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO updatedProductDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(id, updatedProductDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.moveToTrash(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, String> handleProductNotFoundException(NotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return errors;
    }

    @GetMapping("/trash")
    public ResponseEntity<?> getInTrashCategory(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
        return ResponseEntity.ok(productService.getInTrash(PageRequest.of(page -1, limit)));
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id){
        return productService.getById(id);
    }
}
