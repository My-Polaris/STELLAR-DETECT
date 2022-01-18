package com.example.springbootproject.service.impl;

import com.example.springbootproject.model.Roler;
import com.example.springbootproject.repository.RolerRepository;
import com.example.springbootproject.service.RolerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RolerServiceImpl implements RolerService {
    @Resource
    private RolerRepository rolerRepository;

    @Override
    public boolean existsByUserEmail(String userEmail) {
        return rolerRepository.existsByUserEmail(userEmail);
    }

    @Override
    public Roler findByUserEmail(String userEmail) {
        return rolerRepository.findByUserEmail(userEmail);
    }
}
