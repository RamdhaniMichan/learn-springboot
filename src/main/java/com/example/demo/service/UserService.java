package com.example.demo.service;

import org.modelmapper.ModelMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO saveUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public UserDTO updateUser(Long id, UserDTO userDetails) {
        try {
            Optional<User> user = userRepository.findById(id);

            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setName(userDetails.getName());
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

    public Optional<UserDTO> getUserByID(Long id) {
        Optional<User> user = userRepository.findById(id);

        return Optional.ofNullable(modelMapper.map(user, UserDTO.class));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
