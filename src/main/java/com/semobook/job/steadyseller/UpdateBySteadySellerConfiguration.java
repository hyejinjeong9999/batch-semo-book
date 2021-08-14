package com.semobook.job.steadyseller;

import com.semobook.domain.steadyseller.RecomSteadySeller;
import com.semobook.repository.SteadySellerRepository;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UpdateBySteadySellerConfiguration {
    private static final String JOB_NAME = "UpdateBySteadySellerConfiguration";
    private static String KOBO_STEADY_DATA_URL = "http://www.kyobobook.co.kr/bestSellerNew/steadyseller.laf?mallGb=KOR&linkClass=";
    String[] bookTypeList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Q", "R", "S", "T", "U", "V", "a", "b", "c", "d", "e", "f", "g", "08"};
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

    private List<RecomSteadySeller> steadySellerList;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SteadySellerRepository steadySellerRepository;

    @Bean
    public Job steadySellerJob() {
      log.info("Start steadySellerJob");
      return jobBuilderFactory.get(JOB_NAME)
              .start(steadySellerStep())
              .build();
    }
    @Bean
    public Step steadySellerStep() {
        return stepBuilderFactory.get("steadySellerStep")
                .tasklet((contribution, chunkContext) -> {
                    steadySellerBatch();
                    saveSteadySeller();
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
    private void steadySellerBatch() {
        steadySellerList = new ArrayList<>();
        Map<String, Integer> categoryIndex = new HashMap<>();
        for (int i = 0; i <cate.length; i++) {
            categoryIndex.put(cate[i], 1);
        }

        log.info("STEADY SELLER-----");
        for (String s : bookTypeList) {

            String bestUrl = "";
            bestUrl = KOBO_STEADY_DATA_URL + s;

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
                    String category =  CATEGORY_TYPE_MAP.get(s);
                    int idx = categoryIndex.get(category);
                    RecomSteadySeller bookData = RecomSteadySeller.builder()
                            .rank(category + "_" +idx )
                            .isbn(isbn)
                            .bookName(title)
                            .author(author)
                            .publisher(publisher)
                            .kdc("")
                            .category(category)
                            .keyword(detailCategory)
                            .img(imgPath)
                            .build();
                    steadySellerList.add(bookData);
                    categoryIndex.put(category,idx+1);
                    log.info("ISBN = " + isbn + " / BOON NAME = " + title + " / author = " + author + " / publisher = " + publisher + " / imgPath = " + imgPath + " / TITLE CATEGORY = " + titleCategory + " / DETAIL CATEGORY = " + detailCategory);
                } catch (Exception e) {
                    log.error("getBestSellerDa`ta() err :: error msg : {}", e);
                }
            }
        }
    }

    /**
     * save book data
     *
     * @author hyunho
     * @since 2021/08/14
    **/
    private void saveSteadySeller() {
        log.info("saveSteadySeller saveSteadySeller size is = {}", steadySellerList.size());
        steadySellerList.stream().forEach(recomSteadySeller -> steadySellerRepository.save(recomSteadySeller));
    }

}
