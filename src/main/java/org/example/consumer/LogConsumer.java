package org.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.NotificationMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.log}")
    public void consume(NotificationMessage notificationMessage) {
        log.info("Log göndərilir: {} {} {}",
                notificationMessage.getRecipient(),
                notificationMessage.getSubject(),
                notificationMessage.getContent());
    }
}
