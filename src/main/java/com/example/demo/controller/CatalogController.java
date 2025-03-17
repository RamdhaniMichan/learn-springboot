package com.example.demo.controller;

import com.example.demo.dto.CatalogDTO;
import com.example.demo.dto.CatalogRequest;
import com.example.demo.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @PostMapping("/catalog")
    public CatalogDTO createBook(@RequestBody CatalogRequest catalogRequest) {
        CatalogDTO catalog = catalogService.saveCatalog(catalogRequest);

        return ResponseEntity.ok(catalog).getBody();
    }
}
