package com.cliff.trellzo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EMAIL_QUEUE = "email-queue";
    public static final String USER_QUEUE = "user-queue";
    public static final String TRELLZO_EXCHANGE = "trellzoExchange";
    public static final String EMAIL_ROUTING_KEY = "emails_routing_key";
    public static final String USERS_ROUTING_KEY = "users_routing_key";

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE);
    }
    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(TRELLZO_EXCHANGE);
    }

    @Bean
    public Binding emailBinding(@Qualifier("emailQueue") Queue emailQueue,DirectExchange directExchange) {
        return BindingBuilder.bind(emailQueue).to(directExchange).with(EMAIL_ROUTING_KEY);

    }

    @Bean
    public Binding userBinding(@Qualifier("userQueue") Queue userQueue,DirectExchange directExchange) {
        return BindingBuilder.bind(userQueue).to(directExchange).with(USERS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
