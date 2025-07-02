package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.PasswordReset;
import com.dialogdata.backend.repository.PasswordResetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;
    private final UserService userService;

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredPasswordResets() {
        passwordResetRepository.deleteAllByExpiryDateBefore(Instant.now());
    }

    public void deleteById(Integer id) {
        passwordResetRepository.deleteById(id);
    }

    public PasswordReset create(String email) {

        PasswordReset passwordReset = new PasswordReset();

        passwordReset.setUserEmail(email);
        passwordReset.setToken(UUID.randomUUID().toString());
        passwordReset.setExpiryDate(Instant.now().plusSeconds(3600));

        return passwordResetRepository.save(passwordReset);
    }

    public PasswordReset findByEmail(String email) {
        return passwordResetRepository.findByUserEmail(email);
    }

    public boolean verifyToken(String token) {

        PasswordReset passwordReset = passwordResetRepository.findByToken(token);

        if (passwordReset == null || passwordReset.getExpiryDate().isBefore(Instant.now())) {
            return false;
        }

        return true;
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {

        if (!verifyToken(token)) {
            return false;
        }

        PasswordReset passwordReset = passwordResetRepository.findByToken(token);

        if (passwordReset == null) {
            return false;
        }

        userService.updatePassword(passwordReset.getUserEmail(), newPassword);
        deleteById(passwordReset.getId());

        return true;
    }

    public Boolean requestPasswordReset(String email) {

        PasswordReset passwordReset = findByEmail(email);

        if (passwordReset != null) {
            return null;
        }

        if (!userService.existsByEmail(email)) {
            return false;
        }

        return true;
    }
}
