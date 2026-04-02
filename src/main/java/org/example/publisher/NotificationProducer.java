package org.example.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    public void sendMessage(NotificationMessage notificationMessage){
        log.info("Bildiriş göndərilir: {}", notificationMessage.getRecipient());
        rabbitTemplate.convertAndSend(exchange, "", notificationMessage);
    }
}
