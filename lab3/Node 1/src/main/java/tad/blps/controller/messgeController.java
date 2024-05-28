package tad.blps.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping(path = "/send")
    public void sendMesage(
    @RequestParam(name = "message", defaultValue = "hello") String msg) {
// just impl send message between users before
// deleted for testing purpose
    }
}
