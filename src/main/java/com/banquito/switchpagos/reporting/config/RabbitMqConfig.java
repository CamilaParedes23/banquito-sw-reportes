package com.banquito.switchpagos.reporting.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public TopicExchange batchExchange(@Value("${rabbit.exchange.batch}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange routingExchange(@Value("${rabbit.exchange.routing}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange settlementExchange(@Value("${rabbit.exchange.settlement}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange clearingExchange(@Value("${rabbit.exchange.clearing}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange billingExchange(@Value("${rabbit.exchange.billing}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue paymentLinesObserverQueue(@Value("${rabbit.queue.reporting.payment-lines-observer}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue onUsCompletedObserverQueue(@Value("${rabbit.queue.reporting.on-us-completed}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue lineRejectedObserverQueue(@Value("${rabbit.queue.reporting.line-rejected}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue clearingOffUsQueue(@Value("${rabbit.queue.clearing.off-us}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue clearingIncludedQueue(@Value("${rabbit.queue.clearing.included}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue billingBatchCompletedQueue(@Value("${rabbit.queue.billing.batch-completed}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue reportingBillingCompletedQueue(@Value("${rabbit.queue.reporting.billing-completed}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding paymentLinesObserverBinding(
            TopicExchange batchExchange,
            Queue paymentLinesObserverQueue,
            @Value("${rabbit.routing-key.payment-line-requested}") String routingKey) {
        return BindingBuilder.bind(paymentLinesObserverQueue).to(batchExchange).with(routingKey);
    }

    @Bean
    public Binding onUsCompletedObserverBinding(
            TopicExchange settlementExchange,
            Queue onUsCompletedObserverQueue,
            @Value("${rabbit.routing-key.on-us-completed}") String routingKey) {
        return BindingBuilder.bind(onUsCompletedObserverQueue).to(settlementExchange).with(routingKey);
    }

    @Bean
    public Binding lineRejectedFromRoutingBinding(
            TopicExchange routingExchange,
            Queue lineRejectedObserverQueue,
            @Value("${rabbit.routing-key.line-rejected}") String routingKey) {
        return BindingBuilder.bind(lineRejectedObserverQueue).to(routingExchange).with(routingKey);
    }

    @Bean
    public Binding routedOffUsBinding(
            TopicExchange routingExchange,
            Queue clearingOffUsQueue,
            @Value("${rabbit.routing-key.routed-off-us}") String routingKey) {
        return BindingBuilder.bind(clearingOffUsQueue).to(routingExchange).with(routingKey);
    }

    @Bean
    public Binding offUsIncludedBinding(
            TopicExchange clearingExchange,
            Queue clearingIncludedQueue,
            @Value("${rabbit.routing-key.off-us-included}") String routingKey) {
        return BindingBuilder.bind(clearingIncludedQueue).to(clearingExchange).with(routingKey);
    }

    @Bean
    public Binding batchLinesCompletedBinding(
            TopicExchange billingExchange,
            Queue billingBatchCompletedQueue,
            @Value("${rabbit.routing-key.batch-lines-completed}") String routingKey) {
        return BindingBuilder.bind(billingBatchCompletedQueue).to(billingExchange).with(routingKey);
    }

    @Bean
    public Binding billingCompletedBinding(
            TopicExchange billingExchange,
            Queue reportingBillingCompletedQueue,
            @Value("${rabbit.routing-key.billing-completed}") String routingKey) {
        return BindingBuilder.bind(reportingBillingCompletedQueue).to(billingExchange).with(routingKey);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        JacksonJsonMessageConverter messageConverter = new JacksonJsonMessageConverter();
        messageConverter.setTypePrecedence(JacksonJavaTypeMapper.TypePrecedence.INFERRED);
        return messageConverter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
