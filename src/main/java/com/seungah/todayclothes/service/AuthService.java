package com.seungah.todayclothes.service;

import static com.seungah.todayclothes.common.exception.ErrorCode.ALREADY_EXISTS_EMAIL;

import com.seungah.todayclothes.common.exception.CustomException;
import com.seungah.todayclothes.dto.response.CheckAuthKeyResponse;
import com.seungah.todayclothes.entity.Member;
import com.seungah.todayclothes.repository.MemberRepository;
import com.seungah.todayclothes.type.SignUpType;
import com.seungah.todayclothes.type.UserStatus;
import com.seungah.todayclothes.util.AuthKeyRedisUtils;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
	private final MemberRepository memberRepository;
	private final MailService mailService;
	private final AuthKeyRedisUtils authKeyRedisUtils;

	@Transactional
	public void register(String email, String password, String name) {
		if (memberRepository.existsByEmail(email)) {
			throw new CustomException(ALREADY_EXISTS_EMAIL);
		}

		memberRepository.save(Member.builder()
			.email(email)
			.name(name)
			.password(password) // TODO password 인코딩
			.signUpType(SignUpType.EMAIL)
			.userStatus(UserStatus.INACTIVE)
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
		mailService.sendMail(email, subject, text);

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
		log.info(authKeyInRedis);
		if (!authKey.equals(authKeyInRedis)) {
			return new CheckAuthKeyResponse(false);
		}

		authKeyRedisUtils.delete(email);
		return new CheckAuthKeyResponse(true);

	}

}
