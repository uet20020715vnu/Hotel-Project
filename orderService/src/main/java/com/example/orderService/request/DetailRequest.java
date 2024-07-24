package com.example.orderService.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetailRequest {
    private Integer quantity;
    private Integer option;
    private Integer numberOfGuest;
    public DetailRequest(Integer quantity, Integer option){
        this.quantity = quantity;
        this.option = option;
    }
}
