package com.example.springbootproject.repository;

import com.example.springbootproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findAll();
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
