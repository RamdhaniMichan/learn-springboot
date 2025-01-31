package com.example.demo.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private UUID id;

    @NotBlank(message = "name must be required")
    private String username;

    @Email(message = "email should be valid")
    private String email;

    @Nullable
    private String code;
}
