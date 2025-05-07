package com.project.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;
import java.util.List;

@Data
public class FestivalResponse {
    private Head head;

    @JacksonXmlElementWrapper(useWrapping = false)
    //지정된 값을 요소로 묶을 것인지 결정하는 어노테이션, 기본값은 true, row로 각 리스트들을 나열할 것이기에 false로 지정
    private List<Row> row;

    public List<FestivalResponse.Row> getAllFestivals() {
        return row;
    }

    @Data    //이 어노테이션으로 각 필드의 Getter, setter 자동생성.
    public static class Head {
        @JsonProperty("list_total_count")
        private int list_total_count;  // API 출력값 : 행 총 건수

        @JsonProperty("RESULT")
        private Result result;

        @JsonProperty("api_version")
        private String apiVersion;

    }
    @Data    //이 어노테이션으로 각 필드의 Getter, setter 자동생성.
    public static class Result {
        @JsonProperty("CODE")
        private String code;    // API 출력값 : 응답결과코드

        @JsonProperty("MESSAGE")
        private String message; // API 출력값 : 응답결과메세지

    }
    @Data   //이 어노테이션으로 각 필드의 Getter, setter 자동생성.
    public static class Row {

        @JsonProperty("ID")
        private Long id;

        @JsonProperty("INST_NM")    // API 출력값 : 기관 명
        private String instNm;

        @JsonProperty("TITLE")      // API 출력값 : 제목
        private String title;

        @JsonProperty("CATEGORY_NM")    // API 출력값 : 분류
        private String categoryNm;

        @JsonProperty("URL")        // API 출력값 : 주소 URL
        private String url;

        @JsonProperty("ADDR")       // API 출력값 : 행사장 주소
        private String addr;

        @JsonProperty("EVENT_TM_INFO")  // API 출력값 : 행사 시간
        private String eventTmInfo;

        @JsonProperty("BEGIN_DE")       // API 출력값 : 행사 시작 일자
        private String beginDe;

        @JsonProperty("END_DE")         // API 출력값 : 행사 종료 일자
        private String endDe;

        @JsonProperty("IMAGE_URL")      // API 출력값 : 이미지URL
        private String imageUrl;

        @JsonProperty("PARTCPT_EXPN_INFO")  // API 출력값 : 비용
        private String partcptExpnInfo;

        @JsonProperty("TELNO_INFO")     // API 출력값 : 행사 전화번호
        private String telnoInfo;

        @JsonProperty("HOST_INST_NM")   // API 출력값 : 행사 주최 기관 명
        private String hostInstNm;

        @JsonProperty("HMPG_URL")       // API 출력값 : 행사 URL
        private String hmpgUrl;

        @JsonProperty("WRITNG_DE")      // API 출력값 : 작성일
        private String writngDe;

    }

}
