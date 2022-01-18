package com.example.springbootproject.repository;

import com.example.springbootproject.model.Poster;
import com.example.springbootproject.model.Roler;
import com.example.springbootproject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosterRepository extends JpaRepository<Poster,String> {
    List<Poster> findAll();
    @Query("select e from Poster e ORDER BY e.createDate DESC ")
    Page<Poster> findAllOrderByCreateDateDesc(Pageable pageable);
    @Query("select e from Poster e where e.postType=:postType ORDER BY e.createDate DESC ")
    Page<Poster> findByPostTypeOrderByCreateDateDesc(Pageable pageable,@Param("postType") String postType);
}