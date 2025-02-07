package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookServiceInt {
    BookDTO saveBook(BookRequest bookRequest);
    BookDTO updateBook(BookRequest bookRequest, UUID id);
    BookDTO getBookByID(UUID id);
    Page<BookDTO> getAllBook(Pageable pageable, String q);
    BookDTO deleteBookByID(UUID id);
}
