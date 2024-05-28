/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.scheduler.detail;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tad.blps.scheduler.jobs.DeleteFileJob;

/**
 *
 * @author Never
 */
@Configuration
public class DeleteFileDetail {
    @Bean
    public JobDetail DeleteFileDetail() {
        return JobBuilder
                .newJob()
                .ofType(DeleteFileJob.class)
                .storeDurably()
                .withIdentity("Delete_File_Detail")  
                .withDescription("Delete file with max size in system...")
                .build();
    }
}
