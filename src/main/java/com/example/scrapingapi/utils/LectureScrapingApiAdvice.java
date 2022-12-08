package com.example.scrapingapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class LectureScrapingApiAdvice {

//    @ExceptionHandler(value = DataIntegrityViolationException.class)
//    private ResponseEntity sqlIntegrityConstraintViolationException(DataIntegrityViolationException e){
//
//        log.info(e.getClass().getName());
//        log.info("-------------------");
//        log.info(e.getLocalizedMessage());
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 병합 에러 입니다. courseId가 중복됐는지 확인해 보세요.");
//    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseEntity methodArgumentNotValidException (MethodArgumentNotValidException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

//    @ExceptionHandler(value = Exception.class)
//    private ResponseEntity exception(Exception e) {
//        log.info(e.getClass().getName());
//        log.info("-------------------");
//        log.info(e.getLocalizedMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("시스템 로그 확인");
//    }

}
