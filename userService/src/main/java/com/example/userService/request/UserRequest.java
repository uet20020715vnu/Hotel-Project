package com.example.userService.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String address;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
//    private String avatar;
    private String role;
}
