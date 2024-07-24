package com.example.userService.service;


import com.example.userService.dto.UserDTO;
import com.example.userService.request.LoginRequest;
import com.example.userService.request.UserRequest;
import com.example.userService.response.LoginResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDTO> getAll(Pageable pageable);
    Page<UserDTO> getInTrash(Pageable pageable);
    UserDTO createUser(UserRequest userRequest);
//    UserDTO updateUser(Long id, UserRequest userRequest);
    void deleteById(Long id);
    UserDTO findById(Long id);
    UserDTO findByUsername(String username);
    void moveToTrash(Long id);
//    LoginResponse login(LoginRequest userRequest);
}
