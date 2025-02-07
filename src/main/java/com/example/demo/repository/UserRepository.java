package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    @Query(value = "SELECT * FROM users WHERE username = :username LIMIT 1", nativeQuery = true)
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted_at IS NULL")
    Optional<User> findByIdAndDeletedAtIsNull(UUID id);
}
