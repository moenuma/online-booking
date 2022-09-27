package com.moenuma.online.booking.repository;

import com.moenuma.online.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(final String username);

    Boolean existsByUsername(final String username);

    Boolean existsByEmail(final String email);
}
