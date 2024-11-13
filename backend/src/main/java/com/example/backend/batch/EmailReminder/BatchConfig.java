package com.example.backend.batch.EmailReminder;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration("emailReminderBatchConfig")
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EmailReminderReader emailReminderReader;
    private final EmailReminderProcessor emailReminderProcessor;
    private final EmailReminderWriter emailReminderWriter;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                       EmailReminderReader emailReminderReader, EmailReminderProcessor emailReminderProcessor, EmailReminderWriter emailReminderWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.emailReminderReader = emailReminderReader;
        this.emailReminderProcessor = emailReminderProcessor;
        this.emailReminderWriter = emailReminderWriter;
    }

    @Bean
    public Step sendWeeklyReminderStep() {
        return new StepBuilder("sendWeeklyReminderStep", jobRepository)
                .<Object, Map<String, String>>chunk(10,transactionManager)
                .reader(emailReminderReader)
                .processor(emailReminderProcessor)
                .writer(emailReminderWriter)
                .build();
    }

    @Bean
    public Job weeklyReminderJob() {
        return new JobBuilder("weeklyReminderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sendWeeklyReminderStep())
                .build();
    }
}
