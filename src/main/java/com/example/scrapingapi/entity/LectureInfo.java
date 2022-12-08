package com.example.scrapingapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "LECTURE_INFO")
@Builder
public class LectureInfo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_INFO_IDX")
    private Long id;

    @Column(name = "COURSE_ID", unique = true)
    private String courseId;

    @Comment("과목")
    @Column(name = "SUBJECT")
    private String subject;

    @Comment("학습 단계")
    @Column(name = "LEARNING_LEVEL")
    private String learningLevel;

    @Comment("수준")
    @Column(name = "LEVEL")
    private String level;

    @Comment("학년")
    @Column(name = "GRADE")
    private String grade;

    @Comment("선생님 이름")
    @Column(name = "TEACHER_NAME")
    private String teacherName;

    @Comment("강좌 수")
    @Column(name = "LECTURE_COUNT")
    private Long lectureCount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "courseScope", column = @Column(name = "COURSE_SCOPE")),
            @AttributeOverride(name = "courseScopeVal", column = @Column(name = "COURSE_SCOPE_VAL")),
            @AttributeOverride(name = "courseFeatures", column = @Column(name = "COURSE_FEATURE")),
            @AttributeOverride(name = "courseFeaturesVal", column = @Column(name = "COURSE_FEATURE_VAL")),
            @AttributeOverride(name = "recommendedTarget", column = @Column(name = "RECOMMENDED_TARGET")),
            @AttributeOverride(name = "recommendedTargetVal", column = @Column(name = "RECOMMENDED_TARGET_VAL"))
    })
    private LectureIntro lectureIntro;

    @Comment("강의 제목")
    @Column(name = "LECTURE_HEADLINE")
    private String lectureHeadline;

    @Comment("ebsi 링크")
    @Column(name = "EBS_URL")
    private String ebsUrl;

    @OneToMany(mappedBy = "lectureInfo", orphanRemoval = true)
    private List<LectureVideoList> lectureLists = new ArrayList<>();

    public void listCountUpdate (Long lectureCount) {
        this.lectureCount = lectureCount;
    }
}
