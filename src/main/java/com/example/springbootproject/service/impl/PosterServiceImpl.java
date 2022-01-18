package com.example.springbootproject.service.impl;

import com.example.springbootproject.model.Poster;
import com.example.springbootproject.repository.PosterRepository;
import com.example.springbootproject.service.PosterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PosterServiceImpl implements PosterService {
    @Resource
    private PosterRepository posterRepository;

    @Override
    public Page<Poster> findAllOrderByCreateDateDesc(Pageable pageable) {
        return posterRepository.findAllOrderByCreateDateDesc(pageable);
    }

    @Override
    public Page<Poster> findByPostTypeOrderByCreateDateDesc(Pageable pageable, String postType) {
        return posterRepository.findByPostTypeOrderByCreateDateDesc(pageable,postType);
    }

    @Override
    public List<Poster> findAll() {
        return posterRepository.findAll();
    }
}
