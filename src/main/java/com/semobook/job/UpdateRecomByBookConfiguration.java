package com.semobook.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration //Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
public class UpdateRecomByBookConfiguration {



        @Value("${chunkSize:1000}")
        private int chunkSize;

//        public Job updateRecomByBookJob() throws Exception {

//            return jobBuilderFactory.get(JOB_NAME)
//                    .listener(frameMediaJobExecutionListener)
//
//                    .build();
//        }

//        public Step frameMediaGradeByEprsStep(String prdtTpCode, String algmSeq) throws Exception {
//            return stepBuilderFactory.get("frameMediaGradeByEprsStep_"+prdtTpCode)
//                    .listener(frameMediaStepExecutionListener)
//                    .listener(frameMediaChunkListener)
//                    .listener(frameMediaItemReadListener)
//                    .listener(frameMediaItemProcessListener)
//                    .listener(frameMediaItemWriteListener)
//                    .build();
//        }



    }
