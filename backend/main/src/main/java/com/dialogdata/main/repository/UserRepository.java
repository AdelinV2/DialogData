package com.dialogdata.main.repository;

import com.dialogdata.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllBySubscribed(boolean subscribed);
}