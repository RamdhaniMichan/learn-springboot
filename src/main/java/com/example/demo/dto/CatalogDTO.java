package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogDTO {

    private BookDTO book;

    private Integer stock;

    private Integer price;

    private Integer price_discount;

    private Integer discount;

    private Boolean publish;
}
