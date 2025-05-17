package org.example.speaknotebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/api/pdf/**").permitAll()   // PDF 다운로드는 로그인 없이 허용
                        .anyRequest().authenticated()              // 나머지는 로그인 필요
                )
                .formLogin(withDefaults()); // 기본 로그인 폼 제공

        return http.build();
    }
}
