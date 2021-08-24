package com.semobook.scheduler;

import com.semobook.job.book.UpdateBookConfiguration;
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
public class BookScheduler {
    private final JobLauncher jobLauncher;
    private final UpdateBookConfiguration updateBookConfiguration;

//    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60)
    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Seoul")
    public void updateBookJob(){
        JobExecution execution;
        try {
            log.info("start updateBookJob");
            execution = jobLauncher.run(updateBookConfiguration.updateBookJob(), simpleJobParam());
            log.info("Job finished with status : " + execution.getStatus());
            log.info("Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    //같은 이름의 batch는 생길 수 없기 때문에 param에 시간을 넣는다.
    private JobParameters simpleJobParam() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        return new JobParameters(confMap);
    }
}
