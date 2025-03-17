package com.example.demo.repository;

import com.example.demo.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CatalogRepository extends JpaRepository<Catalog, UUID> {
}
