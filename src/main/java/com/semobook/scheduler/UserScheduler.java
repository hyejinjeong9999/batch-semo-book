package com.semobook.scheduler;

import com.semobook.job.UpdateRecomBasedUserInfoConfiguration;
import com.semobook.job.UpdateRecomByBookConfiguration;
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
//스케쥴링 설정하는 곳 여기서 시간에 맞춰서 job이 실행된다
public class UserScheduler {

    private final JobLauncher jobLauncher;
    private final UpdateRecomBasedUserInfoConfiguration updateRecomBasedUserInfoConfiguration;
    private final UpdateRecomByBookConfiguration updateRecomByBookConfiguration;

//    @Scheduled(initialDelay = 1000, fixedDelay = 1000*60) //test -
//    @Scheduled(cron = "0 40 1 * * ?") 매일 한시 사십분에 돌린다
    public void updateRecomBasedUserInfoJob () {
        JobExecution execution;
        try {
            log.info("start updateRecomBasedUserInfoJob");
            execution = jobLauncher.run(updateRecomBasedUserInfoConfiguration.updateRecomBasedUserInfoJob(),simpleJobParam());
//            log.info("Job finished with status : " + execution.getStatus());
            log.info("Current Thread: {}",Thread.currentThread().getName());

        } catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    //     @Scheduled(cron = "0 0 2 * * ?") 매일 두시에 돌린다
   public void updateRecomByBookJob(){
       JobExecution execution;
       try {
           log.info("start updateRecomByBook");
//           execution = jobLauncher.run(updateRecomByBookConfiguration.updateRecomByBookJob(),simpleJobParam());
//           log.info("Job finished with status : " + execution.getStatus());
           log.info("Current Thread: {}",Thread.currentThread().getName());

       } catch (Exception e){
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
