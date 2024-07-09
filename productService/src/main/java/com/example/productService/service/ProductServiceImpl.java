package com.example.productService.service;


import com.example.productService.dto.ProductDTO;
import com.example.productService.entity.Category;
import com.example.productService.entity.Product;
import com.example.productService.exception.NotFoundException;
import com.example.productService.mapper.ProductMapper;
import com.example.productService.repository.CategoryRepository;
import com.example.productService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findByDeletedAtIsNull(pageable);
        List<ProductDTO> productDTOs = productPage.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductDTO> getProductByName(Pageable pageable,String name) {
       return productRepository.findByNameAndDeletedAtIsNull(pageable,name)
               .map(productMapper::toDTO);
    }

    @Override
    public void addProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }

    @Override
    public Page<ProductDTO> findByCategory(Pageable pageable, Category category) {
        return productRepository.findByCategoryAndDeletedAtIsNull(pageable,category).map(productMapper::toDTO);
    }


    @Override
    public void updateProduct(long id, ProductDTO updatedProductDTO) {
//        Optional<Product> existingProduct = productRepository.findById(id);
        updatedProductDTO.setId(id);
        productRepository.save(productMapper.toEntity(updatedProductDTO));

    }


    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }
    @Override
    public void moveToTrash(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("Cannot find this product id: " + id);
        }
        LocalDateTime now = LocalDateTime.now();
        product.setDeletedAt(now);
        productRepository.save(product);
    }

    @Override
    public Page<ProductDTO> getInTrash(Pageable pageable) {
        Page<Product> products = productRepository.findByDeletedAtIsNotNull(pageable);
        return products.map(productMapper::toDTO);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
    }

}