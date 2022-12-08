package com.example.scrapingapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LectureInfoDto {

    private String courseId;

    private String subject;

    private String learningLevel;

    private String level;

    private String grade;
}
