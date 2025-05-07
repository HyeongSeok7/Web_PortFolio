package com.project.web.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/register", "/login", "/main", "/error",       // 비인증 사용자도 접근가능한 엔드포인트
                                "/favicon.ico", "/static/**", "/assets/**", "/css/**", "/js/**", "/images/**", "/fonts/**",
                                "/culture", "/performance", "/exhibition", "/education","/festival/", "/customer", "/search", "festival/"
                        ).permitAll()   //위에 나열된 URL은 인증 없이 접근 가능
                        .anyRequest().authenticated()   // 나머지 모든 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")    // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/main", true)   //로그인 성공 시 리다이렉트 할 URL
                        .failureUrl("/login?error=true")         //로그인 실패 시 리다이렉트 할 URL
                        .permitAll()        //로그인 페이지는 인증 없이 접근 가능
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")   // 로그아웃 엔드포인트
                        .logoutSuccessUrl("/login?logout=true") //로그아웃 성공 후 리다이렉트 할 URL
                        .invalidateHttpSession(true)            //로그아웃 시 세션 무효화
                        .deleteCookies("JSESSIONID")    //로그아웃 시 JSESSIONID 쿠키 삭제
                )
                .sessionManagement(session -> session
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1)         // 동시에 허용되는 최대 세션 수 (1개)
                                .expiredUrl("/login?expired=true")  //세션이 만료되었을 때 리다이렉트 할 URL
                        )
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)    //항상 새로운 세션을 생성
                );

        return http.build();        //설정된 SecurityFilterChain을 반환
    }
}