package com.example.demo.service.specification;

import com.example.demo.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> withFilters(String username, String email) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null) {
                predicates.add(criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }

            if (email != null) {
                predicates.add(criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
