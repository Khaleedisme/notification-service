package org.example.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.NotificationMessage;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void consume(NotificationMessage notificationMessage){
        try{
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(notificationMessage.getRecipient());
            email.setSubject(notificationMessage.getSubject());
            email.setText(notificationMessage.getContent());
            mailSender.send(email);
            log.info("Email göndərilir: {}: {}", notificationMessage.getRecipient(), notificationMessage.getContent());
        } catch (Exception e){
            log.error("Email göndərilmədi: {}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Email uğursuz ", e);
        }
    }
}
