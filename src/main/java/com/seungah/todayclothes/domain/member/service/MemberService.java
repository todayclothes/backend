package com.seungah.todayclothes.domain.member.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_EMAIL;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_MEMBER;

import com.seungah.todayclothes.domain.member.dto.response.CheckAuthNumberResponse;
import com.seungah.todayclothes.domain.member.dto.response.GetProfileResponse;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.member.util.MailUtils;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.redis.util.AuthNumberRedisUtils;
import com.seungah.todayclothes.global.sms.SmsUtils;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final RegionRepository regionRepository;
	private final PasswordEncoder passwordEncoder;
	private final MailUtils mailUtils;
	private final SmsUtils smsUtils;
	private final AuthNumberRedisUtils authNumberRedisUtils;

	@Transactional(readOnly = true)
	public GetProfileResponse getProfile(Long userId) {
		Member member = memberRepository.findByIdWithRegion(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

		return GetProfileResponse.of(member);
	}

	@Transactional
	public GetProfileResponse updateActiveMemberInfo(Long userId, Long regionId, String gender) {
		Member member = memberRepository.getReferenceById(userId);
		Region region = regionRepository.getReferenceById(regionId);

		member.activeMemberInfoUpdate(region, gender);

		return GetProfileResponse.of(member);
	}

	public void sendAuthNumberBySms(Long userId, String phone) {
		if (!memberRepository.existsById(userId)) {
			throw new CustomException(NOT_FOUND_MEMBER);
		}

		String authNumber = issueAuthNumber();
		smsUtils.sendSms(phone, "핸드폰 인증 번호 [" + authNumber + "]를 입력해 주세요.");
		authNumberRedisUtils.put(phone, authNumber);
	}

	@Transactional
	public CheckAuthNumberResponse checkAuthNumberAndSavePhone(
		Long userId, String authNumber, String phone
	) {
		Member member = memberRepository.getReferenceById(userId);

		String authNumberInRedis = authNumberRedisUtils.get(phone);
		if (!authNumber.equals(authNumberInRedis)) {
			return new CheckAuthNumberResponse(false);
		}
		authNumberRedisUtils.delete(phone);
		member.setPhone(phone);
		return new CheckAuthNumberResponse(true);
	}

	@Transactional
	public void updateName(Long userId, String name) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
		member.setName(name);
	}

	@Transactional
	public void updatePassword(Long userId, String password) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
		member.setPassword(passwordEncoder.encode(password));
	}

	@Transactional
	public void findPassword(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(NOT_FOUND_EMAIL));

		String newPassword = UUID.randomUUID().toString(); // 임시로 발급한 패스워드
		String encodedPassword = passwordEncoder.encode(newPassword);

		sendNewPasswordByMail(email, newPassword);
		member.setPassword(encodedPassword);

	}

	private void sendNewPasswordByMail(String email, String newPassword) {

		String subject = "[오늘 뭐 입지?] 회원 임시 비밀번호";
		String text = "<h2>[오늘 뭐 입지?] 회원 임시 비밀번호</h2>\n"
			+ "<p>안녕하세요.<br>귀하의 이메일 주소를 통해 "
			+ "[오늘 뭐 입지?]의 회원 임시 비밀번호가 요청되었습니다.<br>"
			+ "비밀번호는 다음과 같습니다.</p>"
			+ "<h3>" + newPassword + "</h3>"
			+ "<p>회원가입 시 등록한 정보를 수정하려면, [마이페이지]에서 변경하실 수 있습니다.</p>";
		mailUtils.sendMail(email, subject, text);

	}

	private String issueAuthNumber() {
		Random random = new Random();
		StringBuilder authNumber = new StringBuilder();

		for (int i = 0; i < 5; i++) {
			authNumber.append(random.nextInt(9));
		}

		return authNumber.toString();
	}
}


