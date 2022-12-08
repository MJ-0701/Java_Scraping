package com.example.scrapingapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureIntro {

    @Comment("강좌범위")
    private String courseScope;

    @Comment("강좌범위 값")
    private String courseScopeVal;

    @Comment("강좌특징")
    private String courseFeatures;

    @Comment("강좌특징 값")
    private String courseFeaturesVal;

    @Comment("추천대상")
    private String recommendedTarget;

    @Comment("추천대상 값")
    private String recommendedTargetVal;
}
