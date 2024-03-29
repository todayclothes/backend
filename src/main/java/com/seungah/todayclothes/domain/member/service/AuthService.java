package com.seungah.todayclothes.domain.member.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.ALREADY_EXISTS_EMAIL;
import static com.seungah.todayclothes.global.exception.ErrorCode.WRONG_EMAIL_OR_PASSWORD;

import com.seungah.todayclothes.domain.member.dto.response.CheckAuthKeyResponse;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.member.util.MailUtils;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.jwt.JwtProvider;
import com.seungah.todayclothes.global.jwt.TokenDto;
import com.seungah.todayclothes.global.redis.util.AuthKeyRedisUtils;
import com.seungah.todayclothes.global.redis.util.TokenRedisUtils;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.SignUpType;
import com.seungah.todayclothes.global.type.UserStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;
	private final MailUtils mailUtils;
	private final AuthKeyRedisUtils authKeyRedisUtils;
	private final PasswordEncoder passwordEncoder;
	private final TokenRedisUtils tokenRedisUtils;

	@Transactional
	public void register(String email, String password, String name) {

		if (memberRepository.existsByEmail(email)) {
			throw new CustomException(ALREADY_EXISTS_EMAIL);
		}

		Map<ClothesType, Integer> clothesTypeWeights = getDefaultClothesTypeWeights();

		memberRepository.save(Member.builder()
			.email(email)
			.name(name)
			.password(passwordEncoder.encode(password))
			.signUpType(SignUpType.EMAIL)
			.userStatus(UserStatus.INACTIVE)
			.clothesTypeWeights(clothesTypeWeights)
			.build());

	}

	public void sendAuthKeyByMail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new CustomException(ALREADY_EXISTS_EMAIL);
		}

		String authKey = issueAuthKey();

		String subject = "[오늘 뭐 입지?] 회원 가입 인증 코드";
		String text = "<h2>[오늘 뭐 입지?] 회원 가입 인증 코드</h2>\n"
			+ "<p>안녕하세요.<br>귀하의 이메일 주소를 통해 "
			+ "[오늘 뭐 입지?]에 대한 회원가입 인증 코드가 요청되었습니다.<br>"
			+ "인증 코드는 다음과 같습니다.</p>"
			+ "<h3>" + authKey + "</h3>";
		mailUtils.sendMail(email, subject, text);

		authKeyRedisUtils.put(email, authKey);

	}

	private String issueAuthKey() {
		Random random = new Random();
		StringBuilder authKey = new StringBuilder();

		for (int i = 0; i < 5; i++) {
			authKey.append(random.nextInt(9));
		}

		return authKey.toString();
	}

	public CheckAuthKeyResponse checkAuthKey(String email, String authKey) {

		String authKeyInRedis = authKeyRedisUtils.get(email);
		if (!authKey.equals(authKeyInRedis)) {
			return new CheckAuthKeyResponse(false);
		}

		authKeyRedisUtils.delete(email);
		return new CheckAuthKeyResponse(true);

	}

	public TokenDto signIn(String email, String password) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(WRONG_EMAIL_OR_PASSWORD));
		if (!passwordEncoder.matches(password, member.getPassword())) {
			 throw new CustomException(WRONG_EMAIL_OR_PASSWORD);
		}

		return jwtProvider.issueToken(member.getId());
	}

	@Transactional
	public void signOut(Long userId, String accessToken) {
		tokenRedisUtils.delete(userId);
	}

	private static Map<ClothesType, Integer> getDefaultClothesTypeWeights() {
		Map<ClothesType, Integer> clothesTypeWeights = new HashMap<>();
		for (ClothesType clothesType : ClothesType.values()) {
			clothesTypeWeights.put(clothesType, 1);
		}
		return clothesTypeWeights;
	}

}
