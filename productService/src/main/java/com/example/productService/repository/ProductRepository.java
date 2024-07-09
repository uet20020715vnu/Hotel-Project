package com.example.productService.repository;


import com.example.productService.entity.Category;
import com.example.productService.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByNameAndDeletedAtIsNull(Pageable pageable,String name);
    Page<Product> findByCategoryAndDeletedAtIsNull(Pageable pageable, Category category);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByDeletedAtIsNotNull(Pageable pageable);
    Page<Product> findByDeletedAtIsNull(Pageable pageable);
}