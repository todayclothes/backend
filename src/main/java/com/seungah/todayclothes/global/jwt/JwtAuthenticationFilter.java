package com.seungah.todayclothes.global.jwt;

import static com.seungah.todayclothes.global.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static com.seungah.todayclothes.global.exception.ErrorCode.INVALID_TOKEN;
import static com.seungah.todayclothes.global.jwt.JwtProvider.ACCESS_TOKEN_COOKIE_NAME;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtProvider jwtProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		String token = resolveToken((HttpServletRequest) request);

		try {
			if (token != null) {
				Authentication authentication = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (ExpiredJwtException e) {
			log.info("만료된 토큰입니다.");
			request.setAttribute("exception", EXPIRED_ACCESS_TOKEN.getCode());
		} catch (MalformedJwtException e) {
			log.info("손상된 토큰입니다.");
			request.setAttribute("exception", INVALID_TOKEN.getCode());
		} catch (SignatureException e) {
			log.info("시그니처 검증에 실패한 토큰입니다.");
			request.setAttribute("exception", INVALID_TOKEN.getCode());
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 형식이나 구성의 토큰입니다.");
			request.setAttribute("exception", INVALID_TOKEN.getCode());
		} catch (IllegalArgumentException e) {
			log.info("유효하지 않은 토큰입니다.");
			request.setAttribute("exception", INVALID_TOKEN.name());
		}

		chain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String cookies = request.getHeader(HttpHeaders.COOKIE);
		if (cookies == null) {
			return null;
		}

		return cookies.split("; ")[0]
			.replace(ACCESS_TOKEN_COOKIE_NAME + "=", "");
	}
}
