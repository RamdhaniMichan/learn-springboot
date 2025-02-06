package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String message;
    private int code;
    private T data;
    public static final String success = "Success Retrieve Data";
    public static final String userNotFound = "User Not Found";

    // Constructor
    public ApiResponse(String message, int code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, int code, T data) {
        return new ApiResponse<>(message, code, data);
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        return new ApiResponse<>(message, code, null);
    }
}
