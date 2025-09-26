package com.cliff.trellzo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
      System.out.println("Sending email to " + to);
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true);
      javaMailSender.send(mimeMessage);

  }
}
