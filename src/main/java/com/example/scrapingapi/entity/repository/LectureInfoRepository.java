package com.example.scrapingapi.entity.repository;

import com.example.scrapingapi.entity.LectureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LectureInfoRepository extends JpaRepository<LectureInfo, Long> {

    Optional<LectureInfo> findByCourseId(String courseId);
}
