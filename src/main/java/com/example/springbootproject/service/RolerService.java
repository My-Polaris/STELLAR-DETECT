package com.example.springbootproject.service;

import com.example.springbootproject.model.Roler;
import org.springframework.stereotype.Service;

@Service
public interface RolerService {
    boolean existsByUserEmail(String userEmail);
    Roler findByUserEmail(String userEmail);
}
