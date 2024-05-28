/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.RabbitMQ;


import com.rabbitmq.jms.client.message.RMQBytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import jakarta.jms.MessageListener;
/**
 *
 * @author Never
 */
@Component
public class Receiver implements MessageListener {

    @Override
    public void onMessage(Message msg) {
        RMQBytesMessage message = (RMQBytesMessage)msg;
        try {
             System.out.println("Message: " + new String(message.getBody(byte[].class)));
        } catch (JMSException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
