package com.example.productService.service;

import com.example.productService.entity.Hotel;
import com.example.productService.repository.HotelRepository;
import com.example.productService.request.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    public Page<Hotel> findByRequest(Pageable pageable, FilterRequest filterRequest) {
        return null;
    }
}
