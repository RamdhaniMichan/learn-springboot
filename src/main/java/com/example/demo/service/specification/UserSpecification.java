package com.example.demo.service.specification;

import com.example.demo.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> withFilters(String q) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isNull(root.get("deleted_at")));

            if (q != null) {
                Predicate usernamePredicate = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("username")), "%" + q.toLowerCase() + "%");

                Predicate emailPredicate = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("email")), "%" + q.toLowerCase() + "%");

                predicates.add(criteriaBuilder.or(usernamePredicate, emailPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
