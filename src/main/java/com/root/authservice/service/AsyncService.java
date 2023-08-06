package com.root.authservice.service;

import com.root.authservice.utils.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncService {

    private final SendEmailUtil sendEmailUtil;

    @Autowired
    public AsyncService(SendEmailUtil sendEmailUtil){
        this.sendEmailUtil = sendEmailUtil;
    }
    @Async
    public void sendEmail(String otp, String emailId) throws InterruptedException
    {
        System.out.println("Async Call Start");
        sendEmailUtil.sendMail(otp, emailId);
        System.out.println("Async Call End");
    }
}
