package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
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
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody @Valid UserDTO user) {
        UserDTO savedUser = userService.saveUser(user);

        try {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    ApiResponse.success,
                    201,
                    savedUser
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    e.getMessage(),
                    400,
                    null
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);

        ApiResponse<Page<UserDTO>> response = new ApiResponse<>(
                ApiResponse.success,
                200,
                users
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getByUserID(@PathVariable UUID id) {
        Optional<UserDTO> user = userService.getUserByID(id);

        if (user.isPresent()) {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    ApiResponse.success,
                    200,
                    user.get()
            );

            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    ApiResponse.userNotFound,
                    404,
                    null
            );

            return ResponseEntity.status(404).body(response);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserByID(@PathVariable UUID id, @RequestBody @Valid UserDTO userDetails) {

        try {
            UserDTO updateUser = userService.updateUser(id, userDetails);

            ApiResponse<UserDTO> response = new ApiResponse<>(
                    ApiResponse.success,
                    200,
                    updateUser
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    e.getMessage(),
                    400,
                    null
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {

        if (userService.getUserByID(id).isPresent()) {
            userService.deleteUser(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    ApiResponse.success,
                    200,
                    null
            );


            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = new ApiResponse<>(
                    ApiResponse.userNotFound +id,
                    404,
                    null
            );

            return ResponseEntity.status(404).body(response);
        }
    }

}
