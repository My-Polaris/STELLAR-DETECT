package com.example.springbootproject.service.impl;

import com.example.springbootproject.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Override
    @Async
    public boolean sendEmail(String toMail, String subject, String content) {
        try{
            MimeMessage messageM = this.mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(messageM);
            message.setTo(toMail);
            message.setSubject(subject);//发送邮件的标题
            message.setText(content);//发送邮件的内容
            message.setFrom(from);
            mailSender.send(messageM);
        }catch (Exception ex){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
