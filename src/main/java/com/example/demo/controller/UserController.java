package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody @Valid UserRequest user) {
        try {
            UserDTO savedUser = userService.saveUser(user);

            return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, savedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage(), 400));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, users));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getByUserID(@PathVariable UUID id) {
        Optional<UserDTO> user = userService.getUserByID(id);

        return user.map(userDTO -> ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, userDTO))).orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error(ApiResponse.userNotFound, 404)));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserByID(@PathVariable UUID id, @RequestBody @Valid UserDTO userDetails) {

        try {
            UserDTO updateUser = userService.updateUser(id, userDetails);

            return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, updateUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage(), 400));
        }

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {

        if (userService.getUserByID(id).isPresent()) {
            userService.deleteUser(id);

            return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, null));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error(ApiResponse.userNotFound +id, 404));
        }
    }

}
