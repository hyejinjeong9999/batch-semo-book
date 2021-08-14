package com.semobook.job;

import com.semobook.domain.bestseller.RecomBestSeller;
import com.semobook.repository.BestSellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//read process write - chunk
@Slf4j
@RequiredArgsConstructor
@Configuration  //Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
public class UpdateByBestSellerConfiguration {
    private static final String JOB_NAME = "UpdateByBestSellerConfiguration";
    private static String KOBO_BEST_DATA_URL = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=";
    private String[] bookTypeList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Q", "R", "S", "T", "U", "V", "a", "b", "c", "d", "e", "f", "g", "08"};
    String[] cate = {"A", "100", "200", "300", "400", "500", "600", "700", "800", "900"};
    public static final Map<String, String> CATEGORY_TYPE_MAP = new HashMap<String, String>() {
        {
            put("A", "A");
            put("B", "800");
            put("C", "800");
            put("D", "800");
            put("E", "800");
            put("F", "800");
            put("G", "800");
            put("H", "500");
            put("I", "100");
            put("J", "300");
            put("K", "300");
            put("L", "500");
            put("M", "400");
            put("N", "700");
            put("Q", "600");
            put("R", "500");
            put("S", "700");
            put("T", "800");
            put("U", "200");
            put("V", "800");
            put("a", "500");
            put("b", "900");
            put("c", "500");
            put("d", "900");
            put("e", "100");
            put("f", "800");
            put("g", "800");

        }
    };
    private List<RecomBestSeller> bestSellerList;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final BestSellerRepository bestSellerRepository;

    @Bean
    public Job bestSellerJob() throws Exception {
        log.info("Start bestSellerJob");
        return jobBuilderFactory.get(JOB_NAME)
                .start(bestSellerBatchStep())
                .build();

    }

    @Bean
    public Step bestSellerBatchStep() {
        return stepBuilderFactory.get("bestSellerBatchStep")
                //contribution - 현재 단계 실행을 업데이트하기 위해 다시 전달되는 변경 가능한 상태
                // chunkContext - 호출 간에는 공유되지만 재시작 간에는 공유되지 않는 속성
                .tasklet((contribution, chunkContext) -> {
                    bestSellerBatch();
                    saveBestSeller();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    /**
     * crawling book data
     *
     * @author hyunho
     * @since 2021/08/14
     **/
    private void bestSellerBatch() {
        bestSellerList = new ArrayList<>();
        Map<String, Integer> categoryIndex = new HashMap<>();
        for (int i = 0; i < cate.length; i++) {
            categoryIndex.put(cate[i], 1);
        }
        log.info("BEST SELLER-----");
        for (String s : bookTypeList) {

            String bestUrl = "";
            bestUrl = KOBO_BEST_DATA_URL + s;

            Document doc = null;
            try {
                doc = Jsoup.connect(bestUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements elementList = doc.select("#main_contents > ul > li > div.cover > a");
            for (Element element : elementList) {
                //redis 저장
                try {
                    String elementResult = element.attr("href");
                    log.info(element.attr("href"));
                    Document detailDocument = Jsoup.connect(elementResult).get();

                    //ISBN//
                    //Get input tag value (https://stackoverflow.com/questions/46172863/how-to-get-value-of-a-input-field-in-jsoup-in-java)
                    Element elem = Jsoup.parse(String.valueOf(detailDocument.select("#proForm_barcode"))).getElementById("proForm_barcode");
                    String isbn = elem.attr("value");
                    //TITLE CATEGORY//
                    String titleCategory = String.valueOf(detailDocument.select("#container > div.path_bar.clear > div:nth-child(3) > p > span > a").text()).trim();
                    //DETAIL CATEGORY//
                    String detailCategory = String.valueOf(detailDocument.select("#container > div.path_bar.clear > div:nth-child(5) > p > span > a").text());
                    if (detailCategory == null || detailCategory.equals("")) {
                        detailCategory = String.valueOf(detailDocument.select("#container > div.path_bar.clear > div:nth-child(4) > p > span > a").text());
                    }
                    //TITLE//
                    String title = String.valueOf(detailDocument.select("#container > div:nth-child(4) > form > div.box_detail_point > h1 > strong").text());
                    //AUTHOR//
                    String author = String.valueOf(detailDocument.select("#container > div:nth-child(4) > form > div.box_detail_point > div.author > span:nth-child(1) > a").text());
                    //Publisher//
                    String[] resultList = String.valueOf(detailDocument.select("#container > div:nth-child(4) > form > div.box_detail_point > div.author > span > a").text()).trim().split(" ");
                    String publisher = resultList[resultList.length - 1];
                    //IMAGE//
                    String imgPath = String.valueOf(detailDocument.select("#container > div:nth-child(4) > form > div.box_detail_info > div.box_detail_cover > div > a > img").attr("src"));
                    String category = CATEGORY_TYPE_MAP.get(s);
                    int idx = categoryIndex.get(category);
                    bestSellerList.add(RecomBestSeller.builder()
                            .rank(category + "_" + idx)
                            .isbn(isbn)
                            .bookName(title)
                            .author(author)
                            .publisher(publisher)
                            .kdc("")
                            .category(category)
                            .keyword(detailCategory)
                            .img(imgPath)
                            .build());
                    categoryIndex.put(category, idx + 1);
                    log.info("ISBN = " + isbn + " / BOON NAME = " + title + " / author = " + author + " / publisher = " + publisher + " / imgPath = " + imgPath + " / TITLE CATEGORY = " + titleCategory + " / DETAIL CATEGORY = " + detailCategory);
                } catch (Exception e) {
                    log.error("getBestSellerData() err :: error msg : {}", e);
                }
            }
        }
    }

    /**
     * ave book data
     *
     * @author hyunho
     * @since 2021/08/14
     **/
    private void saveBestSeller() {
        bestSellerList.stream().forEach(recomBestSeller -> bestSellerRepository.save(recomBestSeller));
    }
}
