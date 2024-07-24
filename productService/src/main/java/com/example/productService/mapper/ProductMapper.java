package com.example.productService.mapper;


import com.example.productService.dto.ProductDTO;
import com.example.productService.entity.Product;
import com.example.productService.repository.CategoryRepository;
import com.example.productService.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryRepository categoryRepository;
    private final HotelRepository hotelRepository;

    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setDateRefund(product.getDateRefund());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setOwnerId(product.getOwnerId());
        productDTO.setScale(product.getScale());
        productDTO.setCapacity(product.getCapacity());
        productDTO.setHotelId(product.getHotel().getId());
        return productDTO;
    }

    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(categoryRepository.findById(productDTO.getCategoryId()).get());
        product.setQuantity(productDTO.getQuantity());
        product.setOwnerId(productDTO.getOwnerId());
        product.setDateRefund(productDTO.getDateRefund());
        product.setScale(productDTO.getScale());
        product.setCapacity(productDTO.getCapacity());
        product.setHotel(hotelRepository.findById(productDTO.getHotelId()).orElse(null));
        return product;
    }}
