package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.BookDTO;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/book/{id}")
    public Object getBookByID(@PathVariable  UUID id) {
        Optional<BookDTO> book = Optional.ofNullable(bookService.getBookByID(id));

        if (book.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, book));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error("Book not found", 404));
        }
    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<Page<BookDTO>>> getAllBook(Pageable pageable) {
        Page<BookDTO> book = bookService.getAllBook(pageable);
        return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, book));
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<BookDTO>> createBook(BookDTO bookDTO) {
        if (bookDTO.getImage().getSize() > 10 * 1024 * 1024) { // batas ukuran file 10MB
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("File size exceeds the limit", 400));
        }

        if (bookDTO.getImage().getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("Only image files are allowed", 400));
        }

        BookDTO savedBook = bookService.saveBook(bookDTO);

        return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, savedBook));
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> updateBook(@PathVariable UUID id, @RequestBody BookDTO bookDTO) {
        try {
            BookDTO updatedBook = bookService.updateBook(bookDTO, id);

            return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, updatedBook));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage(), 400));
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> deleteBook(@PathVariable UUID id) {
        BookDTO book = bookService.deleteBookByID(id);

        if (book != null) {
            return ResponseEntity.ok(ApiResponse.success(ApiResponse.success, 200, null));
        } else {

            return ResponseEntity.status(404).body(ApiResponse.error("Book not found "+ id, 404));
        }
    }
}
