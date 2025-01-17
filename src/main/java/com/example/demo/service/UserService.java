package com.example.demo.service;

import org.modelmapper.ModelMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO saveUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode("12345678"));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public UserDTO updateUser(UUID id, UserDTO userDetails) {
        try {
            Optional<User> user = userRepository.findById(id);

            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setUsername(userDetails.getUsername());
                existingUser.setEmail(userDetails.getEmail());
                User updateUser = userRepository.save(existingUser);
                return modelMapper.map(updateUser, UserDTO.class);
            } else {
                throw new RuntimeException("User Not Found with ID: " +id);
            }
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Invalid data " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).
                map(user -> modelMapper.map(user, UserDTO.class));
    }

    public Optional<UserDTO> getUserByID(UUID id) {
        Optional<User> user = userRepository.findById(id);

        return Optional.ofNullable(modelMapper.map(user, UserDTO.class));
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> getUserByName(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));

        return Optional.ofNullable(modelMapper.map(user, UserDTO.class));
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        throw new RuntimeException("Invalid username or password");
    }
}
