package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO user) {
        UserDTO savedUser = userService.saveUser(user);

        try {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    "Success Retrieve Data",
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

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);

        ApiResponse<Page<UserDTO>> response = new ApiResponse<>(
                "Success Retrieve Data",
                200,
                users
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getByUserID(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserByID(id);

        if (user.isPresent()) {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    "Success Retrieve Data",
                    200,
                    user.get()
            );

            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserDTO> response = new ApiResponse<>(
                    "User Not Found",
                    404,
                    null
            );

            return ResponseEntity.status(404).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserByID(@PathVariable Long id, @RequestBody UserDTO userDetails) {

        try {
            UserDTO updateUser = userService.updateUser(id, userDetails);

            ApiResponse<UserDTO> response = new ApiResponse<>(
                    "Success Update Data",
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {

        if (userService.getUserByID(id).isPresent()) {
            userService.deleteUser(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    "Success Delete Data",
                    200,
                    null
            );


            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = new ApiResponse<>(
                    "User Not Found with ID " +id,
                    404,
                    null
            );

            return ResponseEntity.status(404).body(response);
        }
    }

}
