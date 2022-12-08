package com.example.scrapingapi.web.controller;

import com.example.scrapingapi.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scraping")
public class LectureScrapingApi {

    private final LectureService lectureService;

    @PostMapping("/create")
    public void lectureScraping(
            @RequestParam String courseId,
            HttpServletResponse response
    ) throws IOException {
        lectureService.saveLectureInfo(courseId);
        response.sendRedirect("https://edumoa.org/admin/lecture_list");

    }

    @PutMapping("/update")
    public void lectureModify (
            @RequestParam String courseId,
            HttpServletResponse response
    ) throws IOException {

      lectureService.listUpdate(courseId);
      response.sendRedirect("https://edumoa.org/admin/lecture_list");
    }




}
