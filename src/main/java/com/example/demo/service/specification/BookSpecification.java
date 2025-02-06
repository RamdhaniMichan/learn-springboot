package com.example.demo.service.specification;

import com.example.demo.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {
    public static Specification<Book> withFilters(String title, String genre) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null) {
                predicates.add(criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("title")), "%" + genre.toLowerCase() + "%"));
            }

            if (genre != null) {
                predicates.add(criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("title")), "%" + genre.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
