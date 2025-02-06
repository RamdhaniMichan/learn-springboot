package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookServiceInt {
    BookDTO saveBook(BookDTO bookDTO);
    BookDTO updateBook(BookDTO bookDTO, UUID id);
    BookDTO getBookByID(UUID id);
    Page<BookDTO> getAllBook(Pageable pageable);
    BookDTO deleteBookByID(UUID id);
}
