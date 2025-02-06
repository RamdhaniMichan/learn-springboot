package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.storage.MinioService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService implements BookServiceInt {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MinioService minioService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public BookDTO saveBook(BookDTO bookDTO) {
        try {
            minioService.uploadFile(bookDTO.getImage().getOriginalFilename(), bookDTO.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Book book = modelMapper.map(bookDTO, Book.class);
        book.setImage(bookDTO.getImage().getOriginalFilename());
        Book savedBook = bookRepository.save(book);
        savedBook.setImage(bookDTO.getImage().getOriginalFilename());
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, UUID id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            Book existingBook = book.get();
            existingBook.setAuthor(bookDTO.getAuthor());
            existingBook.setGenre(bookDTO.getGenre());
            existingBook.setImage(bookDTO.getImage().getOriginalFilename());
            existingBook.setTitle(bookDTO.getTitle());
            existingBook.setDate_publish(bookDTO.getDate_publish());
            Book updatedBook = bookRepository.save(existingBook);
            return modelMapper.map(updatedBook, BookDTO.class);
        } else {
            throw new RuntimeException("Book not found with id "+ id);
        }
    }

    @Override
    public BookDTO getBookByID(UUID id) {
        Optional<Book> book = bookRepository.findById(id);

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public Page<BookDTO> getAllBook(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public BookDTO deleteBookByID(UUID id) {
        bookRepository.deleteById(id);
        return null;
    }
}
