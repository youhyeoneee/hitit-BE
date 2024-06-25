package com.pda.utils.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("env.properties")
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${service.url.fe}")
    private String feUrl;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        System.out.println(feUrl);
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173", feUrl, "http://hitit.site")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfConfig) -> csrfConfig.disable() // .csrf().disable()
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // TODO: mydata, portfolio 링크 정리
                        .requestMatchers("/error", "/api/mydata/**","/api/assets/**", "/api/portfolios/**",
                        "/api/users/signup", "/api/users/login", "/api/users/login/kakao", "api/users/openfeign/**",
                                "/api/users/investment_tests/questions/**", "/api/users/notifications/subscribe/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin.disable()) // formLogin.disable()
                .logout((logoutConfig) -> logoutConfig.permitAll()) //  .logout().permitAll()// 로그아웃 아무나 못하게
                // 사용할 필터와 시기 지정해주기
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider()),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}