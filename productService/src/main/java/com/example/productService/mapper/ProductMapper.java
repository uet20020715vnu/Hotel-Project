package com.example.productService.mapper;


import com.example.productService.dto.ProductDTO;
import com.example.productService.entity.Category;
import com.example.productService.entity.Product;
import com.example.productService.enums.Direction;
import com.example.productService.enums.ProductStatus;
import com.example.productService.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setStatus(product.getStatus());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setArea(productDTO.getArea());
        productDTO.setBedroom(productDTO.getBedroom());
        productDTO.setDirection(product.getDirection());
        productDTO.setFacade(productDTO.getFacade());
        productDTO.setQuantity(productDTO.getQuantity());
        return productDTO;
    }

    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStatus(productDTO.getStatus());
        product.setCategory(categoryRepository.findById(productDTO.getCategoryId()).get());
        product.setArea(productDTO.getArea());
        product.setBedroom(productDTO.getBedroom());
        product.setDirection(productDTO.getDirection());
        product.setFacade(productDTO.getFacade());
        product.setQuantity(productDTO.getQuantity());
        return product;
    }}
