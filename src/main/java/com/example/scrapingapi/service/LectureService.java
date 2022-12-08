package com.example.scrapingapi.service;

import com.example.scrapingapi.entity.LectureInfo;
import com.example.scrapingapi.entity.LectureIntro;
import com.example.scrapingapi.entity.LectureVideoList;
import com.example.scrapingapi.entity.repository.LectureInfoRepository;
import com.example.scrapingapi.entity.repository.LectureVideoListRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {


    private static final String lectureVideoListAjax = "https://www.ebsi.co.kr/ebs/lms/lmsx/courseLecture.ajax";
    private static final String lectureInfoAjax = "https://www.ebsi.co.kr/ebs/lms/lmsx/courseIntro.ajax";
    private static final String ebsUrl = "https://www.ebsi.co.kr/ebs/lms/lmsx/retrieveSbjtDtl.ebs?courseId=";

    private final LectureInfoRepository lectureInfoRepository;
    private final LectureVideoListRepository lectureVideoListRepository;

    @SneakyThrows
    @Transactional
    public void saveLectureInfo(String courseId) {

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
//        Elements elements = basePage.select("body > div.wrap > section > div > div.content > form:nth-child(12) > div.all_lecture_info");
//        elements.forEach(System.out::println);

        Elements elements = basePage.select("body > div.wrap > section > div > div.content > form:nth-child(12) > div.all_lecture_info > div.all_lecture_items");
//        elements.forEach(System.out::println);

        String teacherName = elements.select("strong").text();
//        System.out.println("선생님 : " + teacherName);

        Elements lectureTitle = basePage.select("body > div.wrap > section > div > div.content > form:nth-child(12) > div.all_lecture_info > div.all_lecture_items > div.cont_wrap");
        String headLine = lectureTitle.select("h2[class='tit']").text();
//        System.out.println(headLine);


        // ajax 페이지 크롤링시 일반적인 cssSelector 경로를 복사 하면 안됨
        Document lectureInfoDoc = lectureInfo.post();
        Elements intro = lectureInfoDoc.select("dl[class='cont_info2']");
        Elements elements1 = lectureInfoDoc.select("div[class='view_content']");

//        String lectureIntro = elements2.text(); // -> 리스트를 쭉 나열한 형태
//        System.out.println(lectureIntro);

        Elements dt = intro.select("dt");
        Elements dd = intro.select("dd");


        String subject = dt.select("span[class='label01']").text();
        String learningLevel = dt.select("span[class='label02']").text();
        String level = dt.select("span[class='label03']").text();
        String grade = dt.select("span[class='label04']").text();

        List<TextNode> textNodes = dd.textNodes();
//        textNodes.forEach(System.out::println);

        String subjectVal = textNodes.get(0).text();
        String learningLevelVal = textNodes.get(1).text();
        String levelVal = textNodes.get(2).text();
        String gradeVal = textNodes.get(3).text();

        Document lectureVideoListDoc = lectureVideoList.post();
        Elements list = lectureVideoListDoc.select("li[class='tbody']");


        String scope = "";
        String scopeVal = "";
        String features = "";
        String featuresVal = "";
        String target = "";
        String targetVal = "";

        Elements viewContent = lectureInfoDoc.select("div[class='view_content']");
        Elements group = viewContent.select("div[class='cont_group']");
        Elements title = group.select("p[class='cont_tit']");
        Elements param = group.select("p[class='cont_para']");

        int size = param.size();

        Map<String, String> introMap = new HashMap<>();

        for(int i =0; i<size; i++) {
            String titleVal = title.get(i).text();
            String paramVal = param.get(i).text();
            introMap.put(titleVal, paramVal);
        }

        if(introMap.get("강좌범위") != null) {
//            System.out.println("val : " + introMap.get("강좌범위") );
            scope = "강좌범위";
            scopeVal = introMap.get("강좌범위");
        }

        if (introMap.get("강좌특징") != null) {
//            System.out.println("val : " + introMap.get("강좌특징"));
            features = "강좌특징";
            featuresVal = introMap.get("강좌특징");
        }

        if(introMap.get("추천대상") != null) {
            target = "추천대상";
            targetVal = introMap.get("추천대상");
        }
        LectureIntro lectureIntro1 = LectureIntro
                .builder()
                .courseScope(scope)
                .courseScopeVal(scopeVal)
                .courseFeatures(features)
                .courseFeaturesVal(featuresVal)
                .recommendedTarget(target)
                .recommendedTargetVal(targetVal)
                .build();

        LectureInfo lecInfo = LectureInfo
                .builder()
                .courseId(courseId)
                .subject(subjectVal)
                .learningLevel(learningLevelVal)
                .level(levelVal)
                .grade(gradeVal)
                .teacherName(teacherName)
                .lectureIntro(lectureIntro1)
                .ebsUrl(baseUrl)
                .lectureHeadline(headLine)
                .build();

        lectureInfoRepository.save(lecInfo);
        LectureInfo info = lectureInfoRepository.findByCourseId(courseId).orElseThrow();

        for(Element e : list) {
            String videoId = e.select("div[class='chk_box no_label'] > input[name='chk']").attr("value");
            String videoTitle = e.select("p[class='title']").text();
            String[] videoSplit = videoId.split("/");
            String realVideoId = videoSplit[0];


            LectureVideoList videoList = LectureVideoList
                    .builder()
                    .lectureTitle(videoTitle)
                    .videoId(realVideoId)
                    .lectureInfo(info)
                    .build();
            lectureVideoListRepository.save(videoList);
        }

        videoCountUpdate(info);

        Long listCount = lectureVideoListRepository.countByLectureInfo(info);
        info.listCountUpdate(listCount);
    }

    @SneakyThrows
    @Transactional
    public void listUpdate(String courseId) {

        log.info("courseId : {}", courseId);

        Connection lectureVideoList = Jsoup.connect(lectureVideoListAjax)
                .data("courseId", courseId)
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                ;

        Document lectureVideoListDoc = lectureVideoList.post();
        Elements list = lectureVideoListDoc.select("li[class='tbody']");
        LectureInfo info = lectureInfoRepository.findByCourseId(courseId).orElseThrow();
        videoListBulkDelete(info); // 기존 videoList 전체 삭제

        for(Element e : list) {
            String videoId = e.select("div[class='chk_box no_label'] > input[name='chk']").attr("value");
            String videoTitle = e.select("p[class='title']").text();
            String[] videoSplit = videoId.split("/");
            String realVideoId = videoSplit[0];


            LectureVideoList videoList = LectureVideoList
                    .builder()
                    .lectureTitle(videoTitle)
                    .videoId(realVideoId)
                    .lectureInfo(info)
                    .build();
            lectureVideoListRepository.save(videoList);
        }
    }

    // 기존 videoList bulk delete
    @Transactional
    public void videoListBulkDelete(LectureInfo info) {
        lectureVideoListRepository.deleteAllByLectureInfo(info);
    }

    @Transactional
    public void videoCountUpdate(LectureInfo info) {
        Long listCount = lectureVideoListRepository.countByLectureInfo(info);
        info.listCountUpdate(listCount);
    }


}
