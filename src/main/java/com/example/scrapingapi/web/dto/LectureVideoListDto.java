package com.example.scrapingapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LectureVideoListDto {

    private String lectureTitle;

    private String lectureId;
}
