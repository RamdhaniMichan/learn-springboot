package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.BookRequest;
import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.specification.BookSpecification;
import com.example.demo.storage.MinioService;
import com.example.demo.utility.BeanUtilsHelper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public BookDTO saveBook(BookRequest bookRequest) {
        try {
            minioService.uploadFile(bookRequest.getImage().getOriginalFilename(), bookRequest.getImage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Book book = modelMapper.map(bookRequest, Book.class);
        book.setImage(bookRequest.getImage().getOriginalFilename());
        Book savedBook = bookRepository.save(book);
        savedBook.setImage(bookRequest.getImage().getOriginalFilename());
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    public BookDTO updateBook(BookRequest bookRequest, UUID id) {
        Optional<Book> book = bookRepository.findByIdAndDeletedAtIsNull(id);

        if (book.isPresent()) {
            Book existingBook = book.get();
            BeanUtils.copyProperties(bookRequest, existingBook, BeanUtilsHelper.getNullPropertyNames(bookRequest));
            Book updatedBook = bookRepository.save(existingBook);
            return modelMapper.map(updatedBook, BookDTO.class);
        } else {
            throw new RuntimeException("Book not found with id "+ id);
        }
    }

    @Override
    public BookDTO getBookByID(UUID id) {
        Optional<Book> book = bookRepository.findByIdAndDeletedAtIsNull(id);

        if (book.isPresent()) {
            try {
                Book getBook = book.get();
                String url = minioService.getPresignedUrl(getBook.getImage());
                getBook.setImage(url);
                return modelMapper.map(getBook, BookDTO.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    @Override
    public Page<BookDTO> getAllBook(Pageable pageable, String q) {
        Specification<Book> spec = BookSpecification.withFilters(q);
        return bookRepository.findAll(spec, pageable)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public BookDTO deleteBookByID(UUID id) {
        Optional<Book> book = bookRepository.findByIdAndDeletedAtIsNull(id);
        if (book.isPresent()) {
            Book exisitingBook = book.get();
            exisitingBook.setDeleted_at(LocalDateTime.now());
            bookRepository.save(exisitingBook);
            return null;
        }

        throw new RuntimeException("Book not found");
    }
}
