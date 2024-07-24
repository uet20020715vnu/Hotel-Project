package com.example.userService.service;


import com.example.userService.dto.UserDTO;
import com.example.userService.entity.User;
import com.example.userService.mapper.UserMapper;
import com.example.userService.repository.UserRepository;
import com.example.userService.request.LoginRequest;
import com.example.userService.request.UserRequest;
import com.example.userService.response.LoginResponse;
import com.example.userService.security.jwt.JwtUtils;
import com.example.userService.security.service.UserDetailsImpl;
import com.example.userService.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
//    private final UserDetailsServiceImpl userDetailsService;
//    private final AuthenticationManager authenticationManager;

    public Page<UserDTO> getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findByDeletedAtIsNull(pageable);
        return userPage.map(userMapper::toDTO);
    }

    public Page<UserDTO> getInTrash(Pageable pageable) {
        Page<User> userPage = userRepository.findByDeletedAtIsNotNull(pageable);
        return userPage.map(userMapper::toDTO);
    }
  
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find this user id: " + id);
        }
        return userMapper.toDTO(user);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find this username: " + username);
        }
        return userMapper.toDTO(user);
    }

    public void moveToTrash(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find this user id: " + id);
        }
        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(now);

        userRepository.save(user);
    }

    public UserDTO createUser(UserRequest userRequest) {
        User user = new User();
        String password = encoder.encode(userRequest.getPassword());
        user.setPassword(password);
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setAddress(userRequest.getAddress());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setFirstName(userRequest.getFirstname());
        user.setLastName(userRequest.getLastname());
        user.setGender(userRequest.getGender());
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

//    public UserDTO updateUser(Long id, UserRequest userRequest) {
//        User user = userRepository.findById(id).orElse(null);
//        if (user == null) {
//            return null;
//        }
//
//        String oldPassword = user.getPassword();
//
//        String strRole = userRequest.getRole();
//        Role role;
//
//        if (strRole == null || strRole.isEmpty()) {
//            role = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        } else {
//            switch (strRole) {
//                case "admin" -> {
//                    role = roleRepository.findByName(ERole.ROLE_ADMIN)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                }
//                case "mod" -> {
//                    role = roleRepository.findByName(ERole.ROLE_MODERATOR)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                }
//                default -> {
//                    role = roleRepository.findByName(ERole.ROLE_USER)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                }
//            }
//        }
//
//        if (userRequest.getPassword() != null && userRequest.getPassword().length() >= 6){
//            user.setPassword(encoder.encode(userRequest.getPassword()));
//        } else {
//            user.setPassword(oldPassword);
//        }
//
//        if (userRequest.getRoles() != null && userRequest.getRoles().size() > 0 ){
//            user.setRole(role);
//        }
//
//        if (userRequest.getAvatar() != null && !Objects.equals(user.getAvatar(),
//            userRequest.getAvatar().substring(userRequest.getAvatar().lastIndexOf('/') + 1))){
//            user.setAvatar(userRequest.getAvatar());
//        }
//
//        User savedUser = userRepository.save(user);
//        return UserMapper.INSTANCE.userToUserDTO(savedUser);
//    }
  
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

//    @Override
//    public LoginResponse login(LoginRequest userRequest) {
//            try {
//                LoginResponse loginResponse = new LoginResponse();
//                Authentication authentication = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
//                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
////                jwtUtils.generateJwtToken(authentication);
//                loginResponse.setUsername(userDetails.getUsername());
//                loginResponse.setToken(jwtUtils.generateJwtToken(authentication));
//                return loginResponse;
//            } catch (BadCredentialsException e) {
//                throw new RuntimeException("Invalid username or password");
//            }
//        }
}
