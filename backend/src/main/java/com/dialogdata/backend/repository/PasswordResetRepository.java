package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {

    void deleteAllByExpiryDateBefore(Instant expiryDateBefore);

    PasswordReset findByUserEmail(String userEmail);

    PasswordReset findByToken(String token);
}
