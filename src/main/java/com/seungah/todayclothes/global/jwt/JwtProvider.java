package com.seungah.todayclothes.global.jwt;

import static com.seungah.todayclothes.global.exception.ErrorCode.INVALID_TOKEN;
import static com.seungah.todayclothes.global.jwt.TokenExpirationConfig.getAccessTokenExpirationTime;
import static com.seungah.todayclothes.global.jwt.TokenExpirationConfig.getRefreshTokenExpirationTime;

import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.redis.util.TokenRedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

	public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
	public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
	private static final int CREATE_AGE = 7 * 24 * 60 * 60;

	private final UserDetailsService userDetailsService;
	private final TokenRedisUtils tokenRedisUtils;
	private final Key secretKey;

	public JwtProvider(
		UserDetailsService userDetailsService,
		@Value("${jwt.secret}") String secretKey,
		TokenRedisUtils tokenRedisUtils
	) {
		this.userDetailsService = userDetailsService;
		this.tokenRedisUtils = tokenRedisUtils;

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenDto issueToken(Long userId) {
		Date now = new Date();
		Date accessTokenExpiredDate = new Date(now.getTime() + getAccessTokenExpirationTime());
		Date refreshTokenExpiredDate = new Date(now.getTime() + getRefreshTokenExpirationTime());
		String accessToken = createToken(userId, now, accessTokenExpiredDate);
		String refreshToken = createToken(userId, now, refreshTokenExpiredDate);

		tokenRedisUtils.put(userId, refreshToken, refreshTokenExpiredDate);

		return TokenDto.builder()
			.accessTokenCookie(addCookie(accessToken, ACCESS_TOKEN_COOKIE_NAME))
			.refreshTokenCookie(addCookie(refreshToken, REFRESH_TOKEN_COOKIE_NAME))
			.build();
	}

	private String createToken(Long userId, Date now, Date expiredDate) {
		return Jwts.builder()
			.setSubject(String.valueOf(userId))
			.setIssuedAt(now)
			.setExpiration(expiredDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	private ResponseCookie addCookie(String token, String name) {
		return ResponseCookie.from(name, token)
			.httpOnly(true)
			.path("/")
			.maxAge(CREATE_AGE)
			.secure(true)
			.sameSite(SameSite.NONE.attributeValue())
			.build();
	}

	public Authentication getAuthentication(String accessToken) {
		String userId = this.getUserId(accessToken);
		UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

		return new UsernamePasswordAuthenticationToken(Long.parseLong(userId), "", userDetails.getAuthorities());
	}

	public String getUserId(String accessToken) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build()
			.parseClaimsJws(accessToken).getBody().getSubject();
	}

	public boolean isExpiredToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return false;
		} catch (ExpiredJwtException e) {
			return true;
		} catch (Exception e) {
			throw new CustomException(INVALID_TOKEN);
		}
	}

}
