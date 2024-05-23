/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.WebSocketTransmition;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author Never
 */
@Component
public class JmsReceiver {
    @JmsListener(destination = "messageReceiver")
    public void processMessage(String message){
        System.out.println(message);
    }
}
