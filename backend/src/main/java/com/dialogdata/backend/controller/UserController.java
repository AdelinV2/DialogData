package com.dialogdata.backend.controller;

import com.dialogdata.backend.dto.LoginDto;
import com.dialogdata.backend.dto.UserDto;
import com.dialogdata.backend.entity.User;
import com.dialogdata.backend.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Operation(summary = "Get user by ID", description = "Returns a user by their ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserId(@Parameter(description = "ID of the user", required = true)
                                             @PathVariable("id") Integer id) {

        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Operation(summary = "Create a new user", description = "Creates a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Parameter(description = "User object to be created", required = true)
                                              @RequestBody @Valid UserDto user) {

        User createdUser = userService.createUser(userMapper.toEntity(user));

        if (createdUser == null) {
            return ResponseEntity.status(409).build();
        }

        createdUser.setPassword("null");

        return ResponseEntity.status(201).body(userMapper.toDto(createdUser));
    }

    @Operation(summary = "Update an existing user", description = "Updates an existing user by ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Parameter(description = "ID of the user to be updated", required = true)
                                              @PathVariable("id") Integer id,
                                              @Parameter(description = "Updated user object", required = true)
                                              @RequestBody @Valid UserDto user) {

        User updatedUser = userService.updateUser(id, userMapper.toEntity(user));

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        updatedUser.setPassword("null");

        return ResponseEntity.ok(userMapper.toDto(updatedUser));
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

    @Operation(summary = "Authenticate user", description = "Authenticates a user with email and password")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully")
    @ApiResponse(responseCode = "401", description = "Invalid email or password")
    @PostMapping("/login")
    public ResponseEntity<UserDto> authenticateUser(@Parameter(description = "User login credentials", required = true)
                                                    @RequestBody @Valid LoginDto loginDto) {

        User user = userService.authenticateUser(loginDto);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        user.setPassword("null");

        return ResponseEntity.ok(userMapper.toDto(user));
    }

}
