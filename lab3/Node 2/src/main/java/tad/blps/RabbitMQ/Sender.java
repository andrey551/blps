/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.RabbitMQ;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Never
 */
@Component
public class Sender {
    private Connection conn;

    @Autowired
    public void setConnectionFactory(Connection conn) {
        System.out.println(conn.toString());
        this.conn = conn;

    }

    public void sendMessage(String msg) {
        try {
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("lab3");
            MessageProducer producer = session.createProducer(queue);
            
            Message toSend = session.createTextMessage(msg);
            System.out.println(toSend.toString());
            producer.send(toSend);
//            producer.close();
            session.close();
            
            

        } catch (JMSException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
