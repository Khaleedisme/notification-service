package org.example.consumer;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.example.model.NotificationMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsConsumer {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    @RabbitListener(queues = "${rabbitmq.queue.sms}")
    public void consume(NotificationMessage notificationMessage){
        Twilio.init(accountSid, authToken);
        Message.creator(
                new PhoneNumber(notificationMessage.getRecipient()),
                new PhoneNumber(fromNumber),
                notificationMessage.getContent()
        ).create();
        log.info("SMS göndərilir: {} -> {}",
                notificationMessage.getRecipient(),
                notificationMessage.getContent());
    }
}
