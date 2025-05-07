package com.project.web.controller;


import com.project.web.model.Review;
import com.project.web.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //ReviewController 클래스는 ReviewService를 통해 리뷰와 관련된 요청 처리
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 모든 리뷰 가져오기
    // GET 요청을 통해 저장된 모등 리뷰 반환
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getReviews();
    }

    //새로운 리뷰 추가
    //POST 요청으로 리뷰 내용을 전달받아 저장
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addReview(
            @RequestParam String content,   //리뷰 내용
            @RequestParam String title,     // 관련된 제목 또는 축제 이름
            @AuthenticationPrincipal UserDetails userDetails) { //인증된 사용자의 세부 정보
        try {
            //현재 로그인한 사용자의 이름(ID)를 가져온다.
            String username = userDetails.getUsername();
            //리뷰 서비스를 통해 리뷰를 추가.
            reviewService.addReview(content, username, title);
            //성공적으로 추가 됐을 때 성공 메시지 반환
            return new ResponseEntity<>(Map.of("message", "리뷰를 등록했습니다!!"), HttpStatus.CREATED);
        } catch (Exception e) {
            // 에러 발생 시 에러 메시지를 반환.
            return new ResponseEntity<>(Map.of("message", "리뷰등록에 실패했습니다...: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 특정 제목으로 리뷰 조회
    //GET 요청을 통해 제목(Title)으로 리뷰를 필터링하여 반환.
    @GetMapping("/festival")
    public List<Review> getReviewsByFestival(@RequestParam String title) {  // title로 조회
        return reviewService.getReviewsByTitle(title);
    }

    //특정 축제ID로 리뷰 조회하기
    // 축제 ID를 기반으로 리뷰를 조회하여 반환.
    @GetMapping("/festival/{festivalId}")
    public ResponseEntity<List<Review>> getReviewsByFestivalId(@PathVariable Long festivalId) {
        //리뷰 서비스를 통해 해당 축제ID에 해당하는 리뷰를 가져온다.
        List<Review> reviews = reviewService.getReviewsByFestivalId(festivalId);
        return ResponseEntity.ok(reviews);
    }

    // 특정 축제 제목으로 리뷰 조회
    // URL 경로의 제목(Title)을 기반으로 리뷰를 조회하여 반환.
    @GetMapping("/festival/{title}/reviews")
    public ResponseEntity<List<Review>> getReviewsByFestivalTitle(@PathVariable String title) {
        // 제목을 통해 필터링된 리뷰 리스트를 가져온다.
        List<Review> reviews = reviewService.getReviewsByTitle(title);
        return ResponseEntity.ok(reviews);
    }

}