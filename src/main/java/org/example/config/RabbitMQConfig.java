package org.example.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.email}")
    private String queueEmail;

    @Value("${rabbitmq.queue.sms}")
    private String queueSms;

    @Value("${rabbitmq.queue.log}")
    private String queueLog;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.exchange.dlx}")
    private String dlxExchange;

    @Value("${rabbitmq.queue.dlq}")
    private String dlqQueue;


    @Bean
    public Queue queueEmail(){
        return QueueBuilder.durable(queueEmail)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", dlqQueue)
                .build();
    }

    @Bean
    public Queue queueSms(){
        return QueueBuilder.durable(queueSms)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", dlqQueue)
                .build();
    }

    @Bean
    public Queue queueLog(){
        return QueueBuilder.durable(queueLog)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", dlqQueue)
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(exchange);
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(queueEmail())
                .to(fanoutExchange());
    }

    @Bean
    public Binding smsBinding(){
        return BindingBuilder.bind(queueSms())
                .to(fanoutExchange());
    }

    @Bean
    public Binding logBinding(){
        return BindingBuilder.bind(queueLog())
                .to(fanoutExchange());
    }

    @Bean
    public DirectExchange dlxExchange(){
        return new DirectExchange(dlxExchange);
    }

    @Bean
    public Queue deadLetterQueue(){
        return new Queue(dlqQueue, true);
    }

    @Bean
    public Binding dlqBinding(){
        return BindingBuilder.bind(deadLetterQueue())
                .to(dlxExchange())
                .with(dlqQueue);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) log.info("Mesaj exchange-ə çatdı!");
            else log.error("Mesaj exchange-ə çatmadı: {}", cause);
        });
        return rabbitTemplate;
    }


}
