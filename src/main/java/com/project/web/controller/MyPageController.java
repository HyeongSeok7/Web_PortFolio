package com.project.web.controller;


import com.project.web.model.FavoriteEvent;
import com.project.web.model.FestivalResponse;
import com.project.web.model.User;
import com.project.web.repository.FavoriteEventRepository;
import com.project.web.service.FestivalService;
import com.project.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class MyPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteEventRepository favoriteEventRepository;

    @Autowired
    private FestivalService festivalService;

    @GetMapping("/mypage")
    public String showMyPage(Model model, Principal principal) {

        //현재 로그인한 사용자의 이름 가져오기
        String username = principal.getName();
        User user = userService.getUserByUsername(username);  //사용자 이름을 기반으로 사용자 정보를 가져온다
        model.addAttribute("user", user);       // 사용자 정보를 모델에 추가하여 뷰로 전달

        //사용자의 즐겨찾기 이벤트 조회
        List<FavoriteEvent> favoriteEvents = favoriteEventRepository.findByUsername(username);  //사용자의 즐겨찾기 이벤트를 데이터베이스에서 조회

        //즐겨찾기 이벤트의 ID 목록 추출
        List<String> favoriteEventIds = favoriteEvents.stream()
                .map(FavoriteEvent::getEventId) // FavoriteEvent 객체에서 eventID 필드 값 추출
                .collect(Collectors.toList());  // 스트림 결과를 리스트로 변환

        //즐겨찾기 이벤트의 상세 정보를 조회
        List<FestivalResponse.Row> favoriteEventDetails = festivalService.getFestivalData() // 전체 축제를 가져온다.
                .getRow()   // Row 객체 목록을 가져옴.
                .stream()   // 스트림으로 변환
                .filter(event -> favoriteEventIds.contains(event.getTitle())) // 즐겨찾기한 ID와 일치하는 이벤트 필터링
                .collect(Collectors.toList());  //필터링 결과를 리스트로 변환

        //즐겨찾기 이벤트 상세 정보를 모델에 추가
        model.addAttribute("favoriteEvents", favoriteEventDetails);

        //mypage 뷰 이름 반환
        return "mypage";
    }
}
