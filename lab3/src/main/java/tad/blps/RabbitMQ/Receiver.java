/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.RabbitMQ;

//import com.rabbitmq.client.Connection;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import jakarta.jms.MessageListener;
/**
 *
 * @author Never
 */
@Component
public class Receiver implements MessageListener {
//    private Connection conn;
//
//    @Autowired
//    public void setConnectionFactory(Connection conn) {
//        System.out.println(conn.toString());
//        this.conn = conn;
//
//    }
//
//    public void receiveMessage() {
//        try {
//            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Queue queue = session.createQueue("lab3");
//            MessageConsumer consumer = session.createConsumer(queue);
//
//            TextMessage message = (TextMessage)consumer.receive();
//            System.out.println("Listening...");
//            
////            conn.start();
//            System.out.println(message.getText());
//        } catch (JMSException ex) {
//            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @Override
    public void onMessage(Message msg) {
//        try {
            System.out.println("Message: " + msg);
//        }
//        catch (JMSException ex) {
//            throw new RuntimeException(ex);
//        }

    }
}
