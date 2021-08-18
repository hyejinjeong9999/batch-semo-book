package com.semobook.inactiveUserTest;

//import com.semobook.repository.UserRepository;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class InactiveUserJobTest {

//    @Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;
//
//    @Autowired
//    private UserRepository userRepository;
//    @Test
//    public void abc() throws Exception {
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//
//        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
//        assertEquals(0, userRepository.findAllByLastConnectionBefore(LocalDate.now().minusMonths(3)).size());
//    }
}
