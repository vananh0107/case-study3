package com.example.backend.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeeklyScheduler {

    @Autowired
    private  JobLauncher jobLauncher;

    @Autowired
    private Job weeklyReportJob;

    @Autowired
    private Job weeklyReminderJob;

    @Scheduled(cron = "0 50 15 * * 4")
    public void runWeeklyReport() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(weeklyReportJob, jobParameters);
            jobLauncher.run(weeklyReminderJob, jobParameters);
        } catch (Exception e) {
            System.err.println("Error starting job: " + e.getMessage());
        }
    }
}