package com.example.demo.service;

import com.example.demo.dto.RoleDTO;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRoleRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO saveUser(UserRequest userRequest) {
        Optional<User> getUser = Optional.ofNullable(userRepository.findByUsername(userRequest.getUsername()));

        if (getUser.isPresent()) {
            throw new RuntimeException("User already exists " + userRequest.getUsername());
        }

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode("12345678"));
        User savedUser = userRepository.save(user);
        String paramCode = userRequest.getCode();
        String code = createVariable(paramCode, "admin");

        Role role = roleRepository.findByCode(code);
        if (role == null) {
            throw new RuntimeException("Role not found with code " + code);
        }

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

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
        return userRepository.findAll(pageable)
                        .map(user -> {
                            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                            userDTO.setRoles(user.getUserRoles().stream()
                                    .map(userRole -> modelMapper.map(userRole.getRole(), RoleDTO.class))
                                    .collect(Collectors.toList()));
                            return userDTO;
                        });
    }

    public Optional<UserDTO> getUserByID(UUID id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(userDetail -> {
            UserDTO userDTO = modelMapper.map(userDetail, UserDTO.class);
            userDTO.setRoles(userDetail.getUserRoles().stream()
                    .map(userRole -> modelMapper.map(userRole.getRole(), RoleDTO.class))
                    .collect(Collectors.toList()));
            return userDTO;
        });
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

    public static String createVariable(String param, String defaultValue) {
        return param != null ? param : defaultValue;
    }
}
