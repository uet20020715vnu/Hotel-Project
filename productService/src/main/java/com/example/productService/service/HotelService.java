package com.example.productService.service;

import com.example.productService.entity.Hotel;
import com.example.productService.request.FilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelService {
    Page<Hotel> findByRequest(Pageable pageable, FilterRequest filterRequest);
}
