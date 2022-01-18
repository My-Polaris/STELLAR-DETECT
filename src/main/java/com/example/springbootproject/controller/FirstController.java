package com.example.springbootproject.controller;

import com.example.springbootproject.model.User;
import com.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class FirstController {
    @Resource
    private UserService userService;

    @GetMapping("/")
    public List<User> index(){
        return userService.findAll();
    }
}
