/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
@RequestMapping("/inner")
public class innerController {
    private final FileService fileService;
    private EmailService emailService;
    private UserService userService; 
    private final FileToDelService fileToDelService;

    @Autowired
    public innerController(FileService fileService,
                            EmailService emailService,
                            UserService userService,
                          FileToDelService fileToDelService) {
        this.fileService = fileService;
        this.emailService = emailService;
        this.userService = userService;
        this.fileToDelService = fileToDelService;
    }
    
    @PostMapping("/delFiles")
    public Map<String, List<String>> deleteFiles(@RequestBody @Valid String message) {
        System.out.println(message);
        String toDel = message.substring(2, message.length() - 2);
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
       return usersToSend;
    }
    
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody Map<String, List<String>> mails) {
        for (Map.Entry<String, List<String>> entry : mails.entrySet()) {
            String emailContent = "Your files in this list has deleted: " + entry.getValue().toString();
            String emailSubject = "Notify from tad";
            emailService.sendSimpleMail(new EmailDTO(entry.getKey(), emailContent, emailSubject));
            System.out.println("Sent email");
        }
    }
    
    
}
