package com.example.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    private LocalDateTime deletedAt;
    private String role;
}
