package com.example.productService.service;


import com.example.productService.dto.ProductDTO;
import com.example.productService.entity.Category;
import com.example.productService.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public interface ProductService {
    Page<ProductDTO> getAllProducts(Pageable pageable);
    Page<ProductDTO> getProductByName(Pageable pageable,String name);
    Page<ProductDTO> findByCategory(Pageable pageable, Category category);
    void addProduct(ProductDTO productDTO);
    void updateProduct(long id, ProductDTO updatedProductDTO);
    void deleteProduct(long id);
    void moveToTrash(Long id);
    Page<ProductDTO> getInTrash(Pageable pageable);
    Product getById(Long id);
    ArrayList<Long> getProductRelated(ProductDTO product);
    ProductDTO getByRedis(Long id);
}
