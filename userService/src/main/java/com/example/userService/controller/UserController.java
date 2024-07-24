package com.example.userService.controller;


import com.example.userService.dto.UserDTO;
import com.example.userService.request.LoginRequest;
import com.example.userService.request.UserRequest;
import com.example.userService.security.jwt.JwtUtils;
import com.example.userService.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;

//@Tag(name = "User", description = "User Controller")
//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;


    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ResponseEntity.ok(userService.getAll(PageRequest.of(page - 1, limit)));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(name = "id") Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/trash")
    public ResponseEntity<?> getInTrashUsers(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ResponseEntity.ok(userService.getInTrash(PageRequest.of(page - 1, limit)));
    }

    @GetMapping(path = "/username")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam(name = "username") String username) {
        UserDTO user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequest userRequest) {
        UserDTO newUser = userService.createUser(userRequest);
        return ResponseEntity.ok(newUser);
    }

//    @PutMapping(path = "/{id}")
//    public ResponseEntity<UserDTO> updateUser(@PathVariable(name = "id") Long id, @RequestBody UserRequest userRequest) {
//        UserDTO updatedUser = userService.updateUser(id, userRequest);
//        return ResponseEntity.ok(updatedUser);
//    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) {
        userService.moveToTrash(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(jwtUtils.authenticateUser(loginRequest));
    }

//    @PostMapping(value = "/images",
//            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
//                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//                    MediaType.APPLICATION_JSON_VALUE},
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile imageFile){
//        var dto = fileStorageService.storeUserImageFile(imageFile);
//        return new ResponseEntity<>(dto, HttpStatus.CREATED);
//    }

//    @GetMapping("/images/{filename:.+}")
//    public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
//        Resource resource = fileStorageService.loadUserImageFileAsResource(filename);
//
//        String contentType;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        }catch (Exception ex){
//            throw new CustomException("File not found" + ex, HttpStatus.NOT_FOUND);
//        }
//        if (contentType == null){
//            contentType = "application/octet-stream";
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""
//                        + resource.getFilename() + "\"")
//                .body(resource);
//    }
//
//    @Secured({"ROLE_ADMIN"})
//    @DeleteMapping ("/images/{filename:.+}")
//    public ResponseEntity<?> deleteFile(@PathVariable String filename){
//        fileStorageService.deleteUserImageFile(filename);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}