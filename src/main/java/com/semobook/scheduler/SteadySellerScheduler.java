package com.semobook.scheduler;

import com.semobook.job.steadyseller.UpdateBySteadySellerConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SteadySellerScheduler {
    private final JobLauncher jobLauncher;
    private final UpdateBySteadySellerConfiguration updateBySteadySellerConfiguration;

//    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60) //TEST
    @Scheduled(cron = "0 0 1 * * ?", zone = "Asia/Seoul")
    public void updateBySteadySellerJob(){
        JobExecution execution;
        try {
            log.info("start updateBySteadySellerJob");
            execution = jobLauncher.run(updateBySteadySellerConfiguration.steadySellerJob(), simpleJobParam());
            log.info("Job finished with status : " + execution.getStatus());
            log.info("Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private JobParameters simpleJobParam() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        return new JobParameters(confMap);
    }
}
