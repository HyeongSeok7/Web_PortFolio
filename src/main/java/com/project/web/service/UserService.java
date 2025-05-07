package com.project.web.service;


import com.project.web.model.FavoriteEvent;
import com.project.web.model.User;
import com.project.web.repository.FavoriteEventRepository;
import com.project.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private FavoriteEventRepository favoriteEventRepository;

    @Autowired
    private UserRepository userRepository;

    // 회원가입 메서드
    public void registerUser(String username, String password, String email) {
        // 새로운 User 객체 생성
        User user = new User();

        // 사용자 이름 설정
        user.setUsername(username);

        // 비밀번호를 암호화하여 설정 (passwordEncoder를 사용)
        user.setPassword(passwordEncoder().encode(password));

        // 사용자 이메일 주소 설정
        user.setEmail(email);

        // 가입날짜를 현재 날짜로 설정
        user.setJoinDate(LocalDate.now());

        // User 객체를 데이터베이스에 저장
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 사용자 이름으로 데이터베이스에서 User 객체를 검색
        // 사용자가 없으면 UsernameNotFoundException 예외를 던짐
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Spring Security에서 사용하는 UserDetails 객체를 반환
        // authorities : 현재는 빈 리스트( 권한 없음으로 설정 )
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    // BCryptPasswordEncoder 객체 생성 (비밀번호 암호화 하는데 사용)
    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 사용자의 즐겨찾기 기능을 추가하는 메소드
    public void addFavoriteEvent(String username, String eventId){
        // 새로운 FavoriteEvent 객체를 생성하고 저장소에 저장
        FavoriteEvent favoriteEvent = new FavoriteEvent(username, eventId);
        favoriteEventRepository.save(favoriteEvent);
    }

    // 특정 사용자의 즐겨찾기 이벤트 리스트를 가져오는 메소드
    public List<FavoriteEvent> getFavoriteEvents(String username){
        //사용자 이름으로 즐겨찾기 이벤트를 검색하여 반환
        return favoriteEventRepository.findByUsername(username);
    }

    // 즐겨찾기한 이벤트를 가져오는 메서드
    public List<FavoriteEvent> getFavoriteEventsByUsername(String username) {
        //위 메소드와 동일한 동작을 수행
        return favoriteEventRepository.findByUsername(username);
    }

    //사용자 이름을 기반으로 사용자 정보를 가져오는 메소드
    public User getUserByUsername(String username) {
        //사용자 이름으로 사용자 정보를 검색, 없을 경우 예외 발생
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


}