package com.project.web.controller;

import com.project.web.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // 로그인 페이지를 반환하고, 실패 또는 로그아웃 메시지 추가
    @GetMapping("/login")
    public String moveLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "비밀번호를 확인해주세요");
        }
        if (logout != null) {
            model.addAttribute("message", "로그아웃 되었습니다.");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    // 회원가입 창
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // 회원가입 설정
    @PostMapping("/register")       //HTTP POST 요청을 "/register" URL로 매핑
    public String register(@RequestParam("username") String username,   // 클라이언트가 전송한 "username" 파라미터를 메서드 인자로 바인딩
                           @RequestParam("password") String password,   // 클라이언트가 전송한 "password" 파라미터를 메서드 인자로 바인딩
                           @RequestParam("email") String email, Model model) {  // 뷰로 데이터를 전송하기 위한 Spring MVC의 Model 객체
        userService.registerUser(username, password, email);  //회원가입 로직 실행(username, password, email 전달)
        model.addAttribute("message", "회원가입 성공! 로그인하세요.");
        return "redirect:/login";       //회원가입 성공 훟 "/login" URL로 리다이렉트
    }
}