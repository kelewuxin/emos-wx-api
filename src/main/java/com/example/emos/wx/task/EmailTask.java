package com.example.emos.wx.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/19 10:56
 * @version: 1.0
 */
@Component
@Scope("prototype")
public class EmailTask implements Serializable {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${emos.email.csadress}")
    private String mailbox;

    @Async
    public void sendAsync(SimpleMailMessage message){
        message.setFrom(mailbox);
        message.setCc(mailbox);
        javaMailSender.send(message);
    }
}

