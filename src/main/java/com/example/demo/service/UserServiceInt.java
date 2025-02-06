package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserServiceInt {
    UserDTO saveUser(UserRequest userRequest);
    UserDTO updateUser(UUID id, UserRequest userDetails);
    Page<UserDTO> getAllUsers(Pageable pageable, String username, String email);
    Optional<UserDTO> getUserByID(UUID id);
    void deleteUser(UUID id);
    Optional<UserDTO> getUserByName(String username);
    User login(String username, String password);

}
