package com.example.borimandoo_back.config;

import com.example.borimandoo_back.security.jwt.CustomAuthenticationEntryPoint;
import com.example.borimandoo_back.security.jwt.JwtAuthenticationFilter;
import com.example.borimandoo_back.security.oauth.OAuthLoginFailureHandler;
import com.example.borimandoo_back.security.oauth.OAuthLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customEntryPoint;
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 Origin
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000", "https://medimo.site", "https://borimandoo-kxov0fjrm-golds-projects-6c43d248.vercel.app"));

        // 허용할 HTTP 메서드
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 허용할 헤더
        config.setAllowedHeaders(List.of("*"));

        // Authorization 헤더 사용 가능하도록 설정
        config.setAllowCredentials(true);

        // URL 매핑
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .cors(Customizer.withDefaults())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/healthcheck").permitAll()
                        .requestMatchers("/login", "/oauth2/authorization/**", "/login/oauth2/code/**", "/reissue/access-token").permitAll() // 로그인 및 OAuth 경로는 모두 허용
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/vet/**").hasRole("VET")
                        .requestMatchers("/farmer/**").hasRole("FARMER")
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customEntryPoint))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuthLoginSuccessHandler)  // 로그인 성공 시 핸들러
                        .failureHandler(oAuthLoginFailureHandler)  // 로그인 실패 시 핸들러 (선택)
                );

        return http.build();
    }
}
