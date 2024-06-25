package com.pda.utils.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
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
                        "/api/users/signup", "/api/users/login", "/api/users/login/kakao", "api/openfeign/users/**",
                                "/api/investment_tests/questions/**", "/send/**", "/api/notifications/subscribe/**")
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