/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.config;


import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * @author Never
 */
@Configuration
public class MQTTConfig {
    final String queueName = "lab3";

    final String exchange = "config";

    final String routingkey = "tad";
    
    @Bean
    Queue queue() {
      return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
      return new TopicExchange(exchange);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
      return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public Connection jmsConnectionFactory() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUsername("tad");
        connectionFactory.setPassword("tad");
        connectionFactory.setHost("localhost");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(5672);
        try {
            RMQDestination jmsDestination = new RMQDestination();
            jmsDestination.setAmqp(true);
            jmsDestination.setAmqpQueueName("lab3");
            Connection conn = connectionFactory.createConnection();
            return conn;
        } catch (JMSException ex) {
            Logger.getLogger(MQTTConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
