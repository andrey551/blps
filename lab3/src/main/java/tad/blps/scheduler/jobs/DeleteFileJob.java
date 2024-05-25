/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.scheduler.jobs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tad.blps.RabbitMQ.Sender;
import tad.blps.services.FileService;

/**
 *
 * @author Never
 */
@Component
public class DeleteFileJob implements Job {
    private FileService fileService;
    private Sender sender;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    
    public DeleteFileJob () {
        
    }
    @Autowired
    public void setFileService( FileService fileService) {
        this.fileService = fileService;
    }
    
    @Autowired
    public void setSender( Sender sender) {
        this.sender = sender;
    }
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Long fileDelId = this.fileService.DelBySizemax();
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("src/main/resources/status.log");
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(
                    FileService
                            .class
                            .getName()).
                    log(
                            Level.SEVERE, 
                            null, 
                            ex);
        }

        logger.addHandler(fileHandler);
        logger.log(Level.INFO, 
                "File with Id: {0} has deleted automatically", 
                fileDelId);  
        System.out.println("Test scheduler");
        
        sender.sendMessage("File with Id: " + fileDelId +" has deleted automatically");
    }
    
}
