package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b WHERE b.id = :id AND b.deleted_at IS NULL")
    Optional<Book> findByIdAndDeletedAtIsNull(UUID id);
}
