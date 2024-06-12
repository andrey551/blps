/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import jakarta.jms.MessageListener;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tad.blps.services.EmailService;
import tad.blps.services.FileService;
import tad.blps.services.FileToDelService;
import tad.blps.services.UserService;
/**
 *
 * @author Never
 */
@Component
public class Receiver implements MessageListener {
    private FileService fileService;
    private EmailService emailService;
    
    private UserService userService; 
    private FileToDelService fileToDelService;
    
    public Receiver(FileService fileService, 
            EmailService emailService,
            UserService userService,
            FileToDelService fileToDelService){
        this.fileService = fileService;
        this.emailService = emailService;
        this.userService = userService;
        this.fileToDelService = fileToDelService;
    };
    public Receiver(){};
    @Override
    public void onMessage(Message msg) {
        try {
            String toDel = msg.getStringProperty("message");
            Thread.sleep(1000);
            System.out.println(toDel);
            RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8080/engine-rest/message";
                Map<String, Object> map = new HashMap<>();
                            HttpHeaders headers = new HttpHeaders();
            // set `content-type` header
                headers.setContentType(MediaType.APPLICATION_JSON);
                map.put("messageName","isReceive");
                map.put("businessKey", "tad");
                Map<String, Object> val1 = new HashMap<>();
                Map<String, Object> val2 = new HashMap<>();
                val1.put("value", "true");
                val1.put("type", "boolean");
                val2.put("isReceive", val1);
                map.put("processVariables", val2);
                System.out.print(map.toString());
                ResponseEntity<String> response 
                      = restTemplate.exchange(
                              url, 
                              HttpMethod.POST, 
                              new HttpEntity<>(map, headers), 
                              String.class);
                
        } catch (Exception ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
