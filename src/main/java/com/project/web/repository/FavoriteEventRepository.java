package com.project.web.repository;

import com.project.web.model.FavoriteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {
    List<FavoriteEvent> findByUsername(String username);

    // 특정 사용자 이름으로 모든 즐겨찾기 가져오기
    List<FavoriteEvent> findAllByUsername(String username);

    // 특정 사용자와 이벤트 ID를 기반으로 즐겨찾기 삭제
    void deleteByUsernameAndEventId(String username, String eventId);
}
