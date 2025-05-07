package com.project.web.service;

import com.project.web.model.FestivalResponse;
import com.project.web.model.Review;
import com.project.web.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FestivalService festivalService;

    public ReviewService(ReviewRepository reviewRepository, FestivalService festivalService) {
        this.reviewRepository = reviewRepository;
        this.festivalService = festivalService;
    }

    public Review addReview(String content, String username, String title) {

        // FestivalService를 사용해 축제 제목으로 축제 데이터를 조회
        FestivalResponse.Row festival = festivalService.getFestivalByTitle(title);

        //축제 데이터가 존재하지 않을 경우 예외 발생
        if (festival == null) {
            throw new IllegalArgumentException("Festival not found for title: " + title);
            // 주어진 제목으로 축제를 찾을 수 없다는 예외 메시지 출력
        }

        // 축제 ID 가져오기: 축제 ID가 null인 경우 고유 ID를 생성
        Long festivalId = (festival.getId() != null) ? festival.getId() : generateUniqueFestivalId(title);

        //리뷰 객체 생성 및 데이터 설정
        Review review = new Review();   //새로운 Review 객체 생성
        review.setContent(content);     //리뷰 내용 설정
        review.setTitle(title);         //리뷰가 연결된 축제 제목 설정
        review.setUsername(username);   //리뷰 작성자 설정
        review.setCreatedAt(LocalDateTime.now());   //현재 시간으로 리뷰 생성시간 설정
        review.setFestivalId(festivalId);   // 리뷰가 연결된 축제 ID 설정
        review.setFestivalTitle(festival.getTitle());   //축제 제목 설정

        //리뷰 데이터를 데이터베이스에 저장 및 반환
        return reviewRepository.save(review);   //reviewRepository를 사용해 리뷰 저장
    }
    private Long generateUniqueFestivalId(String title) {
        // Title 기반 고유 해시 생성
        return (long) title.hashCode();
    }

    public List<Review> getReviewsByTitle(String title) {
        return reviewRepository.findByTitle(title);  // title로 필터링
    }

    public List<Review> getReviewsByFestivalId(Long festivalId) {
        return reviewRepository.findByFestivalId(festivalId);
    }

    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }
}