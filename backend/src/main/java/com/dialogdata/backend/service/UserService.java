package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.User;
import com.dialogdata.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Integer id, User user) {

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return null;
        }

        user.setId(id);

        return userRepository.save(user);
    }

    public boolean deleteUserById(Integer id) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        userRepository.delete(user);

        return true;
    }
}
