package com.seungah.todayclothes.global.security;

import static com.seungah.todayclothes.global.type.UserStatus.ACTIVE;
import static com.seungah.todayclothes.global.type.UserStatus.INACTIVE;

import com.seungah.todayclothes.global.jwt.JwtAccessDeniedHandler;
import com.seungah.todayclothes.global.jwt.JwtAuthenticationEntryPoint;
import com.seungah.todayclothes.global.jwt.JwtAuthenticationFilter;
import com.seungah.todayclothes.global.jwt.JwtProvider;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/",
                        "/api/auth/sign-up", "/api/auth/sign-in",
                        "/api/auth/email/auth-key", "/api/auth/email/auth-key/check",
                        "/api/oauth/kakao/callback", "/api/oauth/naver/callback",
                        "/api/members/password/find", "/api/weather/hourly",
                        "/api/weather/daily", "/api/tokens/reissue").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/members/**").hasAnyRole(ACTIVE.name(), INACTIVE.name())
                .antMatchers("/api/**").hasRole(ACTIVE.name());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("OPTIONS", "HEAD", "GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // TODO Header 설정
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
