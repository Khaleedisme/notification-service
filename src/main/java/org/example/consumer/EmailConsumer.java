package org.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.NotificationMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void consume(NotificationMessage notificationMessage){
        log.info("Email göndərilir: {}: {}", notificationMessage.getRecipient(), notificationMessage.getContent());
    }
}
