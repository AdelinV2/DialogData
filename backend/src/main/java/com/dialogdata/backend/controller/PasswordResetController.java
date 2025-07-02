package com.dialogdata.backend.controller;

import com.dialogdata.backend.service.EmailService;
import com.dialogdata.backend.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final EmailService emailService;

    @Operation(summary = "Verify password reset token")
    @ApiResponse(responseCode = "200", description = "Token is valid")
    @ApiResponse(responseCode = "400", description = "Invalid token")
    @GetMapping("/{token}")
    public ResponseEntity<Void> verifyToken(@Parameter(description = "Password reset token", required = true)
                                            @PathVariable("token") String token) {
        boolean isValid = passwordResetService.verifyToken(token);

        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Reset password")
    @ApiResponse(responseCode = "200", description = "Password reset successfully")
    @ApiResponse(responseCode = "400", description = "Invalid password or token")
    @PostMapping("/{token}")
    public ResponseEntity<Void> resetPassword(
            @Parameter(description = "Password reset token", required = true)
            @PathVariable("token") String token,
            @Parameter(description = "New password", required = true)
            @RequestParam("newPassword") String newPassword) {

        boolean isReset = passwordResetService.resetPassword(token, newPassword);

        if (isReset) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Request password reset")
    @ApiResponse(responseCode = "200", description = "Password reset request sent")
    @ApiResponse(responseCode = "208", description = "Password reset request already exists for this email")
    @ApiResponse(responseCode = "400", description = "Invalid email address")
    @PostMapping("/request")
    public ResponseEntity<Void> requestPasswordReset(
            @Parameter(description = "Email address for password reset", required = true)
            @RequestBody String email) {

        Boolean status = passwordResetService.requestPasswordReset(email);

        if (status == null) {
            return ResponseEntity.status(208).build();
        } else if (status) {
            emailService.sendPasswordResetEmail(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
