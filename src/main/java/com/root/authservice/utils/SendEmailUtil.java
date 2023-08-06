package com.root.authservice.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SendEmailUtil {

    public void sendMail(String otp, String email) {
        String content = Constants.EMAIL_TEMPLATE_1 + otp + Constants.EMAIL_TEMPLATE_2;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("quickindustrymail@gmail.com",
                        "qvmdcqsyxadfohde");
            }
        });

        try {
            Message message1 = new MimeMessage(session);
            message1.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message1.setFrom(new InternetAddress("QuickIndustry"));
            message1.setSubject("Password Reset");
            message1.setContent(content, "text/html");
            // send message
            Transport.send(message1);
        } catch (MessagingException e) {
            System.out.println(e.getCause() + "::::::" + e.getMessage());
        }
    }


}
