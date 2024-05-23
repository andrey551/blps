/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tad.blps.WebSocketTransmition.JmsReceiver;
import tad.blps.WebSocketTransmition.XmppSender;

/**
 *
 * @author Never
 */
@RestController
public class messageController {
    private XmppSender xmppService;
    
    private JmsReceiver jmsReceiver;
    
    @Autowired
    public messageController(XmppSender xmppService, JmsReceiver jmsReceiver) {
        this.jmsReceiver = jmsReceiver;
        this.xmppService = xmppService;
    }
    
    @GetMapping("/send")
    public void send(@RequestParam String message,@RequestParam String to) {
        xmppService.sendMessage(to, message);
    }
}
