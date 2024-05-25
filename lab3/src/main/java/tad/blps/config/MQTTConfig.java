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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tad.blps.RabbitMQ.Receiver;


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
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(jmsDestination);
            MessageListener messageListener = new Receiver();
 
            consumer.setMessageListener(messageListener);
            
            conn.start();
            return conn;
        } catch (JMSException ex) {
            Logger.getLogger(MQTTConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
     


    
//    @Bean
//    public SimpleRabbitListenerContainerFactory myFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) throws IOException, TimeoutException {
//            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//            ConnectionFactory connectionFactory = getCustomConnectionFactory();
////            configurer.configure(factory, connectionFactory);
////            factory.setMessageConverter(new jsonMessageConverter());
//            return factory;
//    }
//    @Bean
//    public ConnectionFactory getCustomConnectionFactory() throws IOException, TimeoutException {
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setUsername("tad");
//            factory.setPassword("tad");
//            factory.setVirtualHost("/");
//            factory.setHost("localhost");
//            factory.setPort(5672);
//            String message = "hello from tad";
//            Connection conn = factory.newConnection();
//            Channel channel = conn.createChannel();
//            channel.basicPublish("", "lab3",
//                MessageProperties.PERSISTENT_TEXT_PLAIN,
//                message.getBytes("UTF-8"));
//            return factory;
//    }
    
    public MessageConverter jsonMessageConverter() { 
        return new Jackson2JsonMessageConverter(); 
    }
}
