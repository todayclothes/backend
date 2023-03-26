package com.seungah.todayclothes.configuration;

import static com.seungah.todayclothes.type.UserStatus.ACTIVE;
import static com.seungah.todayclothes.type.UserStatus.INACTIVE;

import com.seungah.todayclothes.common.jwt.JwtAccessDeniedHandler;
import com.seungah.todayclothes.common.jwt.JwtAuthenticationEntryPoint;
import com.seungah.todayclothes.common.jwt.JwtAuthenticationFilter;
import com.seungah.todayclothes.common.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final JwtProvider jwtProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.httpBasic().disable()
			.csrf().disable()
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
				"/api/auth/sign-up",
				"/api/auth/email/auth-key",
				"/api/auth/email/auth-key/check",
				"/api/auth/sign-in").permitAll()

			.and()
			.authorizeRequests()
			// TODO mypage 등록, 수정
			.antMatchers("/api/inactive").hasAnyRole(ACTIVE.name(), INACTIVE.name())
			.antMatchers("/api/**").hasRole(ACTIVE.name());

		return http.build();
	}

}
