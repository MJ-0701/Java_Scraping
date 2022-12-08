package com.example.scrapingapi.service;

import com.example.scrapingapi.entity.repository.LectureInfoRepository;
import com.example.scrapingapi.entity.repository.LectureVideoListRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureServiceTest {

    @Autowired
    private LectureInfoRepository lectureInfoRepository;

    @Autowired
    private LectureVideoListRepository lectureVideoListRepository;

    private static final String lectureVideoListAjax = "https://www.ebsi.co.kr/ebs/lms/lmsx/courseLecture.ajax";
    private static final String lectureInfoAjax = "https://www.ebsi.co.kr/ebs/lms/lmsx/courseIntro.ajax";
    private static final String ebsUrl = "https://www.ebsi.co.kr/ebs/lms/lmsx/retrieveSbjtDtl.ebs?courseId=";

    @Test
    void scrapingTest() throws IOException {
        String courseId = "S20220000550";
        String baseUrl = ebsUrl+courseId;

        Document basePage = Jsoup.connect(baseUrl)
                .get()
                ;

        Connection lectureVideoList = Jsoup.connect(lectureVideoListAjax)
                .data("courseId", courseId)
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                ;

        Connection lectureInfo = Jsoup.connect(lectureInfoAjax)
                .data("courseId", courseId)
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                ;

        // ajax 페이지 크롤링시 일반적인 cssSelector 경로를 복사 하면 안됨
        Document lectureInfoDoc = lectureInfo.post();
        Elements intro = lectureInfoDoc.select("div[class='view_content']");
        Elements group = intro.select("div[class='cont_group']");
        Elements title = group.select("p[class='cont_tit']");
        Elements param = group.select("p[class='cont_para']");

        int size = param.size();
//        System.out.println(size);

        Map<String, String> introMap = new HashMap<>();

        for(int i =0; i<size; i++) {
            String titleVal = title.get(i).text();
            String paramVal = param.get(i).text();
            introMap.put(titleVal, paramVal);
        }

        introMap.forEach((key, val)
                        -> System.out.println("key : " + key + ", val : " + val)
                );


    }

}