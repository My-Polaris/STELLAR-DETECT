package com.example.springbootproject.service;

import com.example.springbootproject.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> findAll();
    boolean existsByEmail(String email);
    User findByEmail(String email);
    User save(User user);
}
