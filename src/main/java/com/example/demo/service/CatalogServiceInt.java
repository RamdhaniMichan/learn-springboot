package com.example.demo.service;

import com.example.demo.dto.CatalogDTO;
import com.example.demo.dto.CatalogRequest;

import java.util.UUID;

public interface CatalogServiceInt {
    CatalogDTO saveCatalog(CatalogRequest catalogRequest);
    CatalogDTO getCatalogById(UUID id);
}
