package com.seungah.todayclothes.service;

import static com.seungah.todayclothes.common.exception.ErrorCode.NOT_FOUND_EMAIL;
import static com.seungah.todayclothes.common.exception.ErrorCode.NOT_FOUND_MEMBER;

import com.seungah.todayclothes.common.exception.CustomException;
import com.seungah.todayclothes.dto.response.GetProfileResponse;
import com.seungah.todayclothes.entity.Member;
import com.seungah.todayclothes.repository.MemberRepository;
import com.seungah.todayclothes.util.MailUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MailUtils mailUtils;
	
	public GetProfileResponse getProfile(Long userId) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

		return GetProfileResponse.of(member);
	}

	@Transactional
	public GetProfileResponse updateGender(Long userId, String gender) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

		return GetProfileResponse.of(member.genderUpdate(gender));
	}

	@Transactional
	public GetProfileResponse updateRegion(Long userId, String region) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

		return GetProfileResponse.of(member.regionUpdate(region));
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
}
