package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogRequest {
    private UUID book_id;

    private Integer stock;

    private Integer price;

    private Integer discount;

    private Boolean publish;
}
