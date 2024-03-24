package com.example.Ott.Recommendation.Program.config;

import com.example.Ott.Recommendation.Program.security.JwtAuthenticationFilter;
import com.example.Ott.Recommendation.Program.security.JwtProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final JwtProvider jwtProvider;

  @Bean
  public SecurityFilterChain filterChain(final @NotNull HttpSecurity http) throws Exception {
    http
        // JWT를 사용하기 때문에 Basic 인증은 필요하지 않으므로 비활성화
        .httpBasic(Customizer.withDefaults())
        // JWT를 사용하는 애플리케이션에서는 일반적으로 CSRF 보호가 필요하지 않음
        .csrf(AbstractHttpConfigurer::disable)
        // CORS 설정 -> 다른 도메인에서도 요청을 허용하기 위한 설정
        .cors(c -> {
              CorsConfigurationSource source = request -> {
                // Cors 허용 패턴 -> 모든 출처(*)와 모든 HTTP 메서드에 대해 요청을 허용하도록 설정하였습니다.
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(
                    List.of("*")
                );
                config.setAllowedMethods(
                    List.of("*")
                );
                return config;
              };
              c.configurationSource(source);
            }
        )
        // JWT 기반 인증에서는 서버 측에 세션을 유지할 필요가 없음
        .sessionManagement(configurer ->
            configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // 조건별로 요청 허용/제한 설정
        .authorizeHttpRequests(authorize -> authorize
            // 회원가입과 로그인은 모두 승인 -> 토큰 없이 이용할 api는 여기에 등록
            .requestMatchers("/login", "/join")
            .permitAll()
            /*
            아래의 로그인 하지 않아도 보여줄 부분은 아래와 같이 사용할 예정
            /public/**: 공개 정보를 제공하는 모든 경로
            /products/**: 상품 목록과 상세 정보를 조회할 수 있는 경로
            /posts/**: 게시글을 조회할 수 있는 경로
             */
            .anyRequest().authenticated())
        
        // JWT 인증 필터 적용
        /*
        .addFilterBefore() 메서드는 Spring Security 필터 체인에 사용자 정의 필터를 추가
        여기서는 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 실행 전에 추가
        인증 메커니즘으로 JWT를 사용한다는 것을 의미하며, 요청이 들어올 때마다 JWT의 유효성을 검사하여 사용자가 인증되었는지 확인
        JwtAuthenticationFilter는 jwtProvider를 사용하여 들어오는 요청의 JWT를 검증
        JWT가 유효하면, 필터는 요청을 처리하기 위해 요청 컨텍스트에 사용자 인증 정보를 설정
         */
        .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
            UsernamePasswordAuthenticationFilter.class)


        // 에러 핸들링
        .exceptionHandling(authenticationManager -> authenticationManager
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(401);
              response.setCharacterEncoding("utf-8");
              response.setContentType("text/html; charset=UTF-8");
              response.getWriter().write("인증되지 않은 사용자입니다.");
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setStatus(403);
              response.setCharacterEncoding("utf-8");
              response.setContentType("text/html; charset=UTF-8");
              response.getWriter().write("권한이 없는 사용자입니다.");
            })
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}