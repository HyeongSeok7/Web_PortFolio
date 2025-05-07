package com.project.web.controller;


import com.project.web.model.FavoriteEvent;
import com.project.web.model.FestivalResponse;

import com.project.web.repository.FavoriteEventRepository;
import com.project.web.service.FestivalService;
import com.project.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FestivalController {

    @Autowired
    private FavoriteEventRepository favoriteEventRepository;


    @Autowired
    private UserService userService;

    private final FestivalService festivalService;

    @Autowired
    public FestivalController(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "main";
    }


        //상제페이지 제목을 기반으로 상세페이지 생성 로직
        @GetMapping("/festival/{title:.+}")
        public String festivalDetail(@PathVariable String title, Model model) {
            try {
                // URL 디코딩을 통해 UTF-8 형식으로 제목을 디코딩
                String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8.name());
                System.out.println("Decoded title: " + decodedTitle);

                // 제목을 정규화하여 일관된 형식으로 변환
                String normalizedTitle = festivalService.normalize(decodedTitle);
                System.out.println("Normalized title: " + normalizedTitle);

                // 정규화된 제목을 기반으로 Festival 데이터를 가져옴
                FestivalResponse.Row festival = festivalService.getFestivalByTitle(normalizedTitle);
                if (festival == null) {
                    //제목에 해당하는 축제가 없을 경우 예외를 발생시킴
                    throw new IllegalArgumentException("Festival not found for title: " + normalizedTitle);
                }

                //모델에 festival 속성을 추가하여 뷰에서 사용할 수 있도록 설정
                model.addAttribute("festival", festival);
                return "festivalDetail";    // "festivalDetail.html"뷰를 렌더링 함
            } catch (Exception e) {
                e.printStackTrace();
                return "errorpage"; // 에러 발생 시 errorpage.html로 이동
            }
        }


        // 즐겨찾기 이벤트 추가
    @PostMapping("/addFavoriteEvent")
    @ResponseBody
    public ResponseEntity<?> addFavoriteEvent(@RequestBody Map<String, String> payload, Principal principal) {
        // 인증된 사용자의 이름을 가져옴
        String username = principal.getName();
        // 요청 본문으로 이벤트Id 를 가져옴
        String eventId = payload.get("event_id"); // 필드 이름을 event_id로 변경

        // FavoriteEvent 객체를 생성하여 저장
        FavoriteEvent favoriteEvent = new FavoriteEvent(username, eventId);
        favoriteEventRepository.save(favoriteEvent);

        // 성공메시지 반환
        return ResponseEntity.ok("즐겨찾기에 추가되었습니다.");
    }

    // 사용자의 즐겨찾기 이벤트 조회
    @GetMapping("/getFavoriteEvents")
    @ResponseBody
    public ResponseEntity<?> getFavoriteEvents(Principal principal) {
        // 인증된 사용자의 이름을 가져옴
        String username = principal.getName();

        //사용자의 즐겨찾기 이벤트 ID 리스트를 가져옴
        List<String> favoriteEventIds = favoriteEventRepository.findAllByUsername(username)
                .stream()
                .map(FavoriteEvent::getEventId)
                .collect(Collectors.toList());
        //즐겨찾기 이벤트 ID 리스트를 반환
        return ResponseEntity.ok(favoriteEventIds);
    }

    //즐겨찾기 이벤트 삭제
    @DeleteMapping("/removeFavoriteEvent")
    @ResponseBody
    public ResponseEntity<?> removeFavoriteEvent(@RequestBody Map<String, String> payload, Principal principal) {
        //인증된 사용자의 이름을 가져옴.
        String username = principal.getName();
        // 요청 본문에서 이벤트 ID를 가져옴
        String eventId = payload.get("event_id");

        //해당 사용자의 특정 이벤트를 즐겨찾기에서 삭제
        favoriteEventRepository.deleteByUsernameAndEventId(username, eventId);
        //성공메시지 반환
        return ResponseEntity.ok("즐겨찾기에서 제거되었습니다.");
    }
}
