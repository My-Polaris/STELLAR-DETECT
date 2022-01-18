package com.example.springbootproject.service;

import com.example.springbootproject.model.Poster;
import com.example.springbootproject.model.Roler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PosterService {
    List<Poster> findAll();
    Page<Poster> findAllOrderByCreateDateDesc(Pageable pageable);
    Page<Poster> findByPostTypeOrderByCreateDateDesc(Pageable pageable,String postType);
}
