package com.example.scrapingapi.entity.repository;

import com.example.scrapingapi.entity.LectureInfo;
import com.example.scrapingapi.entity.LectureVideoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LectureVideoListRepository extends JpaRepository<LectureVideoList, Long> {

    Optional<LectureVideoList> findByLectureInfo(LectureInfo lectureInfo);

    @Modifying
    @Query("DELETE FROM LectureVideoList v WHERE v.lectureInfo = :lectureInfo")
    void deleteAllByLectureInfo(LectureInfo lectureInfo);

    Long countByLectureInfo(LectureInfo lectureInfo);

}
