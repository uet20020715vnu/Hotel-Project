package com.example.productService.entity;

import com.example.productService.entity.Base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "hotel")
    private List<Product> product;
//    @OneToMany(mappedBy = "hotel")
//    private List<Option> option;
    private Long ownerId;
    private String address;
    private String addressDetail;
    private String phoneNumber;

}
