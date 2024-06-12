/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.config;


import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.Session;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;


import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tad.blps.RabbitMQ.Receiver;
import tad.blps.services.EmailService;
import tad.blps.services.FileService;
import tad.blps.services.FileToDelService;
import tad.blps.services.UserService;


/**
 *
 * @author Never
 */
@Configuration
public class MQTTConfig {
    private FileService fileService;
    private EmailService emailService; 
    private UserService userService;
    
    private FileToDelService fileToDelService;
    
    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
        System.out.println("set file service");
    }
    
    @Autowired
    public void setEmailService( EmailService emailService) {
        this.emailService = emailService;
        System.out.println("set email service");
    }
    
    @Autowired 
    public void setUserService( UserService userService) {
        this.userService = userService;
        System.out.println("set user service");
    }
    
    @Autowired
    public void setFileToDelService( FileToDelService fileToDelService) {
        this.fileToDelService = fileToDelService;
        System.out.println("set file to Del service");
    }
    
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
        connectionFactory.setUsername("ta");
        connectionFactory.setPassword("ta");
        connectionFactory.setHost("localhost");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(5672);
        try {
            RMQDestination jmsDestination = new RMQDestination();
            jmsDestination.setAmqp(true);
            jmsDestination.setAmqpQueueName("lab3");
            Connection conn = connectionFactory.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(jmsDestination);
            MessageListener messageListener = new Receiver(
                                                            fileService, 
                                                            emailService,
                                                            userService,
                                                            fileToDelService);
            
            
            consumer.setMessageListener(messageListener);
          
            conn.start();
            return conn;
        } catch (JMSException ex) {
            Logger.getLogger(MQTTConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
