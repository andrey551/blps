/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.RabbitMQ;

import jakarta.jms.Message;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.jms.MessageListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tad.blps.DTO.EmailDTO;
import tad.blps.entity.File;
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
             toDel = toDel.substring(1, toDel.length() - 1);
             List<File> files = new ArrayList<>();
             
             for( String s : toDel.split(", ")) {
                 File t = fileService.getFileById(Long.valueOf(s));
                 System.out.println(t.getUserId());
                 files.add(t);  // to get information about user and file name
             }
             
             Map<String, List<String>> usersToSend  = new HashMap<>(); // to create email content
             files.forEach(file  -> {
                 String addr = userService.getById(file.getUserId()).getEmail();
                 System.out.println(addr);
                 if(!usersToSend.containsKey(addr)) {
                     usersToSend.put(addr, new ArrayList<>());
                 }
                 usersToSend.get(addr).add(file.getFilename_source());
             });
             
            for( String s : toDel.split(", ")) {
                 fileService.deleteById(Long.valueOf(s)); // delete file from request
                 fileToDelService.deleteFile(Long.valueOf(s));
                 System.out.println("File with id " + s + " has successfully deleted");
             }
            
            for (Map.Entry<String, List<String>> entry : usersToSend.entrySet()) {
                 String emailContent = "Your files in this list has deleted: " + entry.getValue().toString();
                 String emailSubject = "Notify from tad";
                 emailService.sendSimpleMail(new EmailDTO(entry.getKey(), emailContent, emailSubject));
                 System.out.println("Sent email");
             }
             
        } catch (Exception ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
