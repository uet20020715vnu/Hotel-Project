package com.example.productService.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private String address;
    private Integer numberOfGuest;
    private LocalDate startDate;
    private LocalDate endDate;
}
