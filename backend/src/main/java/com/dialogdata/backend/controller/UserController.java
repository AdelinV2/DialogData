package com.dialogdata.backend.controller;

import com.dialogdata.backend.dto.UserDto;
import com.dialogdata.backend.entity.User;
import com.dialogdata.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Returns a user by their ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserId(@Parameter(description = "ID of the user", required = true)
                                          @PathVariable("id") Integer id) {

        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create a new user", description = "Creates a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping
    public ResponseEntity<User> createUser(@Parameter(description = "User object to be created", required = true)
                                           @RequestBody @Valid UserDto user) {

        return ResponseEntity.status(201).body(userService.createUser(user.toEntity()));
    }

    @Operation(summary = "Update an existing user", description = "Updates an existing user by ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Parameter(description = "ID of the user to be updated", required = true)
                                           @PathVariable("id") Integer id,
                                           @Parameter(description = "Updated user object", required = true)
                                           @RequestBody @Valid UserDto user) {

        User updatedUser = userService.updateUser(id, user.toEntity());

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the user to be deleted", required = true)
                                           @PathVariable("id") Integer id) {

        boolean isDeleted = userService.deleteUserById(id);

        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
