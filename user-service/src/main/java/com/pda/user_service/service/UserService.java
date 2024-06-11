package com.pda.user_service.service;

import com.pda.user_service.dto.KaKaoTokenDto;
import com.pda.user_service.dto.KakaoUserDto;
import com.pda.user_service.dto.UserDto;
import com.pda.user_service.jpa.User;
import com.pda.user_service.jpa.UserRepository;
import com.pda.utils.exception.DuplicatedEmailException;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@PropertySource(value = {"env.properties"})
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.KAUTH_TOKEN_URL_HOST}")
    private String KAUTH_TOKEN_URL_HOST;
    @Value("${kakao.KAUTH_USER_URL_HOST}")
    private String KAUTH_USER_URL_HOST;



    public User signup(UserDto userDto) throws DuplicatedEmailException {
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
        KaKaoTokenDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
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

    public String getUserInfo(String accessToken) {
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
            userRepository.save(user);
            log.info("회원가입");
        }
        log.info("로그인");
        return accessToken;
    }
}
