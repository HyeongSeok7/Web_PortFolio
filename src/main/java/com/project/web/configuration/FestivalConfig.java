package com.project.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class FestivalConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
