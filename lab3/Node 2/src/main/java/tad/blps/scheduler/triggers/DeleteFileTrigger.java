/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.scheduler.triggers;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Never
 */

@Configuration
public class DeleteFileTrigger {
    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
          .withIdentity("Delete_File_Detail")
          .withDescription("Delete file with max size in system...")
          .withSchedule(dailyAtHourAndMinute(1, 26))
          .build();
    }
}
