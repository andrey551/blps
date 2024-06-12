package tad.blps.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tad.blps.RabbitMQ.Sender;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Never
 */

@RestController
@RequestMapping("/message")
public class messgeController {
    private Sender sender;
    
    @Autowired 
    public messgeController(Sender sender) {
        this.sender = sender;
    }
    @PostMapping(path = "/send")
    public void sendMesage(
    @RequestBody String message) {
            System.out.println(message);
            message = message.substring(13, message.length() - 3);
            List<Long> files = new ArrayList<>();
             
            for( String s : message.split(", ")) {
                files.add(Long.valueOf(s));  // to get information about user and file name
            }
            
            sender.sendMessage(files);
    }
}
