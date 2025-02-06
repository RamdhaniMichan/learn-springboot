package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "genre is required")
    private String genre;

    private String date_publish;

    @NotBlank(message = "author is required")
    private String author;

    @NotBlank(message = "image is required")
    private MultipartFile image;
}
