package com.example.productService.repository;

import com.example.productService.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    List<Category> findByName(String name);

    // Hoặc sử dụng @Query nếu muốn tùy chỉnh truy vấn
    @Query(value = "SELECT * FROM Category WHERE c.name = :name",nativeQuery = true)
    List<Category> findByNameUsingQuery(@Param("name") String name);

    Page<Category> findByDeletedAtIsNotNull(Pageable pageable);
    Page<Category> findByDeletedAtIsNull(Pageable pageable);
}
