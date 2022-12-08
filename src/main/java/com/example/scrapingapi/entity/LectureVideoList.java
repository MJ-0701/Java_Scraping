package com.example.scrapingapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "LECTURE_VIDEO_LIST")
@Builder
public class LectureVideoList extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_LIST_IDX")
    private Long id;

    @Comment("강의 제목")
    @Column(name = "LECTURE_TITLE")
    private String lectureTitle;

    @Comment("동영상 ID")
    @Column(name = "LECTURE_ID")
    private String videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LECTURE_INFO_IDX")
    private LectureInfo lectureInfo;

}
