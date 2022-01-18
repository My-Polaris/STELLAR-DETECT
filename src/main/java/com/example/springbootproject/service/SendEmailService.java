package com.example.springbootproject.service;

import org.springframework.stereotype.Service;

@Service
public interface SendEmailService {
    boolean sendEmail(String toMail,String subject,String content);
}

