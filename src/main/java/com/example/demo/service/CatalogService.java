package com.example.demo.service;

import com.example.demo.dto.CatalogDTO;
import com.example.demo.dto.CatalogRequest;
import com.example.demo.entity.Book;
import com.example.demo.entity.Catalog;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CatalogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CatalogService implements CatalogServiceInt{

    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CatalogDTO saveCatalog(CatalogRequest catalogRequest) {
        Optional<Book> book = bookRepository.findByIdAndDeletedAtIsNull(catalogRequest.getBook_id());
        if (book.isPresent()) {
            Book existingBook = book.get();
            Catalog catalog = modelMapper.map(catalogRequest, Catalog.class);
            catalog.setBook(existingBook);
            Catalog savedCatalog = catalogRepository.save(catalog);
            CatalogDTO responseBook = modelMapper.map(savedCatalog, CatalogDTO.class);
            responseBook.setPrice_discount(countDiscount(catalogRequest.getPrice(), catalogRequest.getDiscount()));
            return responseBook;
        }

        throw new RuntimeException("Book not found");
    }

    @Override
    public CatalogDTO getCatalogById(UUID id) {
        return null;
    }

    private Integer countDiscount(Integer price, Integer discount) {
        double setDiscount = discount;
        double presentation = setDiscount / 100.0;
        return (Integer) (int) (price - (presentation * price));
    }
}
