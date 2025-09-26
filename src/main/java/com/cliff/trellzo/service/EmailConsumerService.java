package com.cliff.trellzo.service;

import com.cliff.trellzo.config.RabbitMqConfig;
import com.cliff.trellzo.dto.email.EmailQueueDto;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumerService {

    private final EmailService emailService;

    public EmailConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = {RabbitMqConfig.EMAIL_QUEUE})
    public void processEmail(EmailQueueDto emailQueueDto){
        System.out.println("Email Received "+emailQueueDto);

        // now send the email
        try {
            emailService.sendEmail(emailQueueDto.getEmail(), emailQueueDto.getSubject(), emailQueueDto.getPayload());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
