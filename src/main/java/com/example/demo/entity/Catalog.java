package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "catalogs")
@Setter
@Getter
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true, updatable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer stock;

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer price;

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer discount;

    @Setter
    @Getter
    private Boolean publish;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updated_at;

    private LocalDateTime deleted_at;
}
