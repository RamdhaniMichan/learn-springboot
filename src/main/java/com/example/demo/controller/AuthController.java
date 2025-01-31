package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);

        String roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getCode()).collect(Collectors.joining(","));

        String token = jwtUtil.generateToken(user.getUsername(), roles);

        ApiResponse<?> response = new ApiResponse<>(
                "Success Login",
                200,
                token
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/hello")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<String> hello(@RequestParam String name) {
        return ResponseEntity.ok("Hello " + name);
    }
}
