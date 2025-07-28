package com.example.borimandoo_back.security.oauth;

import com.example.borimandoo_back.domain.RefreshToken;
import com.example.borimandoo_back.domain.User;
import com.example.borimandoo_back.repository.RefreshTokenRepository;
import com.example.borimandoo_back.repository.UserRepository;
import com.example.borimandoo_back.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.redirect.success-uri}")
    private String REDIRECT_URI;  // ex) https://your-frontend.com/login/success?userId=%s&name=%s&accessToken=%s&refreshToken=%s

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String provider = token.getAuthorizedClientRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;
        if ("kakao".equals(provider)) {
            oAuth2UserInfo = new KakaoUserInfo(token.getPrincipal().getAttributes());
        }

        if (oAuth2UserInfo == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth provider: " + provider);
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();

        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            user = User.builder()
                    .userId(UUID.randomUUID())
                    .name(name)
                    .role(User.Role.BASIC)
                    .subscription(User.Subscription.Basic)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else {
            refreshTokenRepository.deleteByUserId(user.getUserId());
        }

        // JWT 발급
        String role = user.getRole().name(); // 또는 .toString() — enum 타입이면

        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), role, REFRESH_TOKEN_EXPIRATION_TIME);
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), role, ACCESS_TOKEN_EXPIRATION_TIME);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(user.getUserId())
                        .token(refreshToken)
                        .build()
        );

        // 프론트에 넘겨줄 이름 URL 인코딩
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

        // 리다이렉트 URI에 토큰 붙이기
        String formattedRedirectUri = String.format(REDIRECT_URI,
                user.getUserId(), encodedName, accessToken, refreshToken);

        log.info("✅ 최종 리다이렉트 URI: {}", formattedRedirectUri);

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, formattedRedirectUri);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
