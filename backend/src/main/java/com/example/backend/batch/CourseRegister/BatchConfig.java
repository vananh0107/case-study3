package com.example.backend.batch.CourseRegister;

import com.example.backend.dto.CourseDTO;
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

@Configuration("courseRegisterBatchConfig")
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager
                               transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Step generateWeeklyReportStep() {
        return new StepBuilder("generateWeeklyReportStep", jobRepository)
                .<DataReader, Map<CourseDTO, Long>>chunk(10,transactionManager)
                .reader(courseRegisterReader())
                .processor(courseRegisterProcessor())
                .writer(courseWriter())
                .build();
    }

    @Bean
    public Job weeklyReportJob() {
        return new JobBuilder("weeklyReportJob", jobRepository)
                .start(generateWeeklyReportStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public CourseRegisterReader courseRegisterReader() {
        return new CourseRegisterReader();
    }

    @Bean
    public CourseRegisterProcessor courseRegisterProcessor() {
        return new CourseRegisterProcessor();
    }

    @Bean
    public CourseRegisterWriter courseWriter() {
        return new CourseRegisterWriter();
    }
}