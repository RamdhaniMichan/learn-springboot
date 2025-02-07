package com.example.demo.service.specification;

import com.example.demo.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {
    public static Specification<Book> withFilters(String q) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isNull(root.get("deleted_at")));

            if (q != null) {
                Predicate titlePredicate = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("title")), "%" + q.toLowerCase() + "%");

                Predicate genrePredicate = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("genre")), "%" + q.toLowerCase() + "%");

                predicates.add(criteriaBuilder.or(titlePredicate, genrePredicate));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
