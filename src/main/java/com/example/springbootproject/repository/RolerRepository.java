package com.example.springbootproject.repository;

import com.example.springbootproject.model.Roler;
import com.example.springbootproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolerRepository extends JpaRepository<Roler,String> {
    boolean existsByUserEmail(String userEmail);
    Roler findByUserEmail(String userEmail);
}
