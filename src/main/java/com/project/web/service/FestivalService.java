package com.project.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.project.web.model.FestivalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FestivalService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public FestivalResponse getFestivalData() {
        String response = restTemplate.getForObject(apiUrl + "?apikey=" + apiKey, String.class);

        XmlMapper xmlMapper = new XmlMapper();
        try {
            FestivalResponse festivalResponse = xmlMapper.readValue(response, FestivalResponse.class);

            if (festivalResponse != null && festivalResponse.getAllFestivals() != null) {
                // 제목을 기준으로 중복 제거된 리스트 생성
                List<FestivalResponse.Row> distinctFestivals = festivalResponse.getAllFestivals().stream()
                        .collect(Collectors.toMap(
                                FestivalResponse.Row::getTitle,  // 제목(title) 을 키로 사용
                                Function.identity(),             // 각 Row 자체를 값으로 사용
                                (existing, replacement) -> existing  // 중복 시 기존 값 유지
                        ))
                        .values()
                        .stream()
                        .collect(Collectors.toList());

                festivalResponse.setRow(distinctFestivals);  // 중복 제거된 리스트로 설정
            }

            return festivalResponse;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 검색 메서드: 제목, 기관명 을 기준으로 검색
    public List<FestivalResponse.Row> searchFestivals(String keyword) {
        // 축제 데이터를 가져옴
        FestivalResponse festivalResponse = getFestivalData();

        // 축제 데이터가 null이거나, 데이터 안의 축제 리스트가
        if (festivalResponse == null || festivalResponse.getAllFestivals() == null) {
            return List.of(); // null 또는 데이터가 없는 경우 빈 리스트 반환
        }

        // 축제 리스트를 스트림으로 처리하여 조건에 맞는 데이터를 필터링
        return festivalResponse.getAllFestivals().stream()
                .filter(festival ->
                        // 축제 제목이 null 이 아니고, 제목에 키워드가 포함된 경우
                        (festival.getTitle() != null && festival.getTitle().contains(keyword)) ||
                        // 축제 기관명이 null이 아니고, 기관명에 키워드가 포함된 경우
                                (festival.getInstNm() != null && festival.getInstNm().contains(keyword)))
                //조건에 맞는 축제를 리스트로 반환
                .collect(Collectors.toList());
    }


    // 상세페이지 (더 알아보기) 설정
    public FestivalResponse.Row getFestivalByTitle(String title) {
        // 입력된 제목을 표준화 (예: 대소문자 구분 제거)
        String normalizedTitle = normalize(title);

        // 디버깅을 위해 표준화된 제목 출력
        System.out.println("Normalized input title: " + normalizedTitle);

        // 축제 데이터를 가져오고, 스트림으로 필터링
        return getFestivalData().getRow().stream()
                // 각 축제의 제목을 표준화하여 디버깅용으로 출력
                .peek(festival -> System.out.println("Normalized festival title: " + normalize(festival.getTitle())))
                 // 축제의 표준화된 제목과 입력된 표준화된 제목을 비교하여 필터링
                .filter(festival -> normalize(festival.getTitle()).equals(normalizedTitle))
                // 첫 번쨰로 일치하는 축제를 반환, 없으면 예외를 발생
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Festival not found for title: " + title));
    }

    // 문자열을 표준화 하는 메소드
    // 특정 문자만 남기고 제거 , 공백 제거, 소문자로 변환
    public String normalize(String input) {
        // 입력 값이 null 이면 빈 문자열 ("") 를 반환
        if (input == null) return "";

        //문자열에서 허용된 문자만 남기고 제거한 후 표준화
        return input
                .replaceAll("[^a-zA-Z0-9가-힣\\[\\]']", "") // 알파벳, 숫자, 한글, 특정 특수문자('[]'를 제외한 나머지 제거
                .replace(" ", "") // 공백 제거
                .toLowerCase(); // 모든 알파벳 문자들을 소문자로 변환
    }



}
