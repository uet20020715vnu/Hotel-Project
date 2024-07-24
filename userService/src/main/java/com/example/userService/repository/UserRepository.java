package com.example.userService.repository;

import com.example.userService.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Page<User> findByDeletedAtIsNull(Pageable pageable);
    Page<User> findByDeletedAtIsNotNull(Pageable pageable);
}
