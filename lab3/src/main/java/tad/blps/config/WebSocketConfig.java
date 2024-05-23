/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author Never
 */
@Configuration
@EnableJms
public class WebSocketConfig {
    @Bean 
    public ActiveMQConnectionFactory conenctionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
    
    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(conenctionFactory());
    }
    
}
