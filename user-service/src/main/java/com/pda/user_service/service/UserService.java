package com.pda.user_service.service;

import com.pda.user_service.dto.*;
import com.pda.user_service.jpa.User;
import com.pda.user_service.jpa.UserRepository;
import com.pda.utils.exception.DuplicatedEmailException;
import com.pda.utils.exception.login.NotCorrectPasswordException;
import com.pda.utils.exception.login.NotFoundUserException;
import com.pda.utils.security.dto.UserDetailsDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@PropertySource(value = {"env.properties"})
public class UserService { //implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.client_secret}")
    private String clientSecret;
    @Value("${kakao.KAUTH_TOKEN_URL_HOST}")
    private String KAUTH_TOKEN_URL_HOST;
    @Value("${kakao.KAUTH_USER_URL_HOST}")
    private String KAUTH_USER_URL_HOST;



    @Transactional
    public User signup(SignupUserDto userDto) throws DuplicatedEmailException {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        User user = userDto.toEntity();

        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            return userRepository.save(user);
        }
        else {
            throw new DuplicatedEmailException("이미 가입된 이메일입니다.");
        }
    }

    public String getAccessTokenFromKakao(String code) {
        log.info(code);
        log.info(clientSecret);
        KaKaoTokenDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .queryParam("client_secret", clientSecret)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                // TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KaKaoTokenDto.class)
                .block();

        return kakaoTokenResponseDto.getAccessToken();
    }

    @Transactional
    public User getUserInfo(String accessToken) {
        KakaoUserDto kakaoUserDTO = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                // TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserDto.class).block();

        User user = kakaoUserDTO.toEntity(accessToken);

        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            User savedUser = userRepository.save(user);
            log.info("회원가입");
        }

        Optional<User> resultUser = userRepository.findByEmail(user.getEmail());
        return resultUser.orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public User login(LoginDto loginDto)  throws NotFoundUserException, NotCorrectPasswordException {
            Optional<User> resultUser = userRepository.findByEmail(loginDto.getEmail());
        if (resultUser.isEmpty()) {
            log.error("User not found with email: " + loginDto.getEmail());
            throw new NotFoundUserException("등록되지 않은 이메일입니다. 이메일을 확인해주세요.");
        }
        if (passwordEncoder.matches(loginDto.getPassword(),  resultUser.get().getPassword())) {
            return resultUser.get();
        }
        else {
            throw new NotCorrectPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }


    @Transactional
    public void updateInvestmentTypeAndScore(int userId, String investmentType, int investmentTestScore) { // throws UsernameNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
        user.setInvestmentType(investmentType);
        user.setInvestmentTestScore(investmentTestScore);
        userRepository.save(user);
    }

    public User findUserById(int userId) throws NotFoundUserException {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("사용자가 존재하지 않습니다."));
    }

    public User updateUser(int userId, UserUpdateRequestDto dto) throws NotFoundUserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("사용자가 존재하지 않습니다."));

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getBirthdate() != null) {
            user.setBirthdate(dto.getBirthdate());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getInvestmentType() != null) {
            user.setInvestmentType(dto.getInvestmentType());
        }
        if (dto.getInvestmentTestScore() != null) {
            user.setInvestmentTestScore(dto.getInvestmentTestScore());
        }
        if (dto.getMydata() != null) {
            user.setMydata(dto.getMydata());
        }
        if (dto.getPortfolio() != null) {
            user.setPortfolio(dto.getPortfolio());
        }

        userRepository.save(user);

        User savedUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
        return savedUser;
    }

    public UserDetailsDto convert2UserDetailsDto(User user) throws NotFoundUserException {
        // User 엔티티를 UserDetailsDto로 변환
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setUsername(String.valueOf(user.getId()));
        userDetailsDto.setPassword(user.getPassword());
        userDetailsDto.setAuthorities(null); // 별도의 권한 없음  // Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")
        userDetailsDto.setAccountNonExpired(true);
        userDetailsDto.setAccountNonLocked(true);
        userDetailsDto.setCredentialsNonExpired(true);
        userDetailsDto.setEnabled(true);

        return userDetailsDto;
    }
}
