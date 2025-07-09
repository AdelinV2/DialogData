package com.dialogdata.main.repository;

import com.dialogdata.main.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {

    void deleteAllByExpiryDateBefore(Instant expiryDateBefore);

    PasswordReset findByUserEmail(String userEmail);

    PasswordReset findByToken(String token);
}
