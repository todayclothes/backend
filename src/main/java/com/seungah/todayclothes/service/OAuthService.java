package com.seungah.todayclothes.service;

import static com.seungah.todayclothes.common.exception.ErrorCode.NOT_MATCH_SIGNUP_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungah.todayclothes.common.exception.CustomException;
import com.seungah.todayclothes.common.jwt.JwtProvider;
import com.seungah.todayclothes.dto.OAuthVendor;
import com.seungah.todayclothes.dto.TokenDto;
import com.seungah.todayclothes.dto.request.SocialSignInRequest;
import com.seungah.todayclothes.entity.Member;
import com.seungah.todayclothes.repository.MemberRepository;
import com.seungah.todayclothes.type.SignUpType;
import com.seungah.todayclothes.type.UserStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthVendor oAuthVendor;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    public TokenDto socialSignIn(String vendor, String code, String state, HttpServletResponse response) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(vendor, code, state);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        SocialSignInRequest socialUserInfo = getSocialUserInfo(vendor, accessToken);

        // 3. 필요시에 회원가입
        // 우리 DB 에도 저장이 되어야 하니까, 만약 저장되어 있다면 그냥 넘어가고, 없다면 회원 저장을 해야함.
        Member socialMember = registerSocialUserIfNeeded(vendor, socialUserInfo);

        // 4. JWT 토큰 반환
        return jwtProvider.issueToken(socialMember.getId());
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getToken(String vendor, String code, String state) throws JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        Map<String, String> switchVendor = switchVendor(vendor);

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", switchVendor.get("clientId"));
        body.add("redirect_uri", switchVendor.get("redirectUri"));
        body.add("client_secret", switchVendor.get("clientSecret"));
        body.add("code", code);
        body.add("state", state);

        String tokenUri = switchVendor.get("tokenUri");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> socialTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                tokenUri,
                HttpMethod.POST,
                socialTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private SocialSignInRequest getSocialUserInfo(String vendor, String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> socialUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                switchVendor(vendor).get("userInfoUri"),
                HttpMethod.POST,
                socialUserInfoRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        System.out.print(jsonNode);

        String id = "";
        String email = "";
        String nickname = "";
        switch (vendor) {
            case "kakao":
                if (jsonNode.get("id") != null) {
                    id = jsonNode.get("id").asText();
                } else {
                    log.warn("Failed to get ID from Kakao JSON response.");
                }
                if (jsonNode.get("kakao_account") != null && jsonNode.get("kakao_account").get("email") != null) {
                    email = jsonNode.get("kakao_account").get("email").asText();
                } else {
                    log.warn("Failed to get email from Kakao JSON response.");
                }
                if (jsonNode.get("properties") != null && jsonNode.get("properties").get("nickname") != null) {
                    nickname = jsonNode.get("properties").get("nickname").asText();
                } else {
                    log.warn("Failed to get nickname from Kakao JSON response.");
                }
                break;
            case "google":
                if (jsonNode.get("sub") != null) {
                    id = jsonNode.get("sub").asText();
                } else {
                    log.warn("Failed to get ID from Google JSON response.");
                }
                if (jsonNode.get("email") != null) {
                    email = jsonNode.get("email").asText();
                } else {
                    log.warn("Failed to get email from Google JSON response.");
                }
                if (jsonNode.get("name") != null) {
                    nickname = jsonNode.get("name").asText();
                } else {
                    log.warn("Failed to get nickname from Google JSON response.");
                }
                break;
            case "naver":
                if (jsonNode.get("response") != null) {
                    JsonNode responseNode = jsonNode.get("response");
                    if (responseNode.get("id") != null) {
                        id = responseNode.get("id").asText();
                    } else {
                        log.warn("Failed to get ID from Naver JSON response.");
                    }
                    if (responseNode.get("email") != null) {
                        email = responseNode.get("email").asText();
                    } else {
                        log.warn("Failed to get email from Naver JSON response.");
                    }
                    if (responseNode.get("nickname") != null) {
                        nickname = responseNode.get("nickname").asText();
                    } else {
                        log.warn("Failed to get nickname from Naver JSON response.");
                    }
                } else {
                    log.warn("Failed to get response from Naver JSON response.");
                }
                break;
        }
        log.info("소셜로그인 사용자 정보: " + id + ", " + nickname + ", " + email);
        return SocialSignInRequest.builder()
                .id(id)
                .nickname(nickname)
                .userEmail(email)
                .build();
    }

    // 3. 필요시에 회원가입
    private Member registerSocialUserIfNeeded(String vendor, SocialSignInRequest socialUserInfo) {

        // DB 에 중복된 Kakao Id 가 있는지 확인
        String socialId = socialUserInfo.getId();

        Member socialMember = null;
        switch (vendor) {
            case "kakao":
                socialMember = memberRepository.findByKakaoUserId(Long.valueOf(socialId)).orElse(null);
                break;
            case "naver":
                socialMember = memberRepository.findByNaverUserId(socialId).orElse(null);
                break;
            default:
                break;
        }

        if (socialMember == null) {
            // 소셜로그인 사용자 email 동일한 email 가진 회원이 있는지 확인
            String socialEmail = socialUserInfo.getUserEmail();
            Member sameEmailMember = memberRepository.findByEmail(socialEmail).orElse(null);

            if (sameEmailMember != null) {
                socialMember = sameEmailMember;

                // 기존 회원정보에 소셜로그인 Id 추가
                putSocialId(vendor,socialMember,socialId);
                socialMember = memberRepository.save(socialMember);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: 소셜로그인 email
                String userEmail = socialUserInfo.getUserEmail();

                socialMember = Member.builder()
                        .email(userEmail)
                        .password(encodedPassword)
                        .userStatus(UserStatus.INACTIVE)
                        .name(socialUserInfo.getNickname())
                        .signUpType(getSignUpType(vendor))
                        .build();
                putSocialId(vendor,socialMember,socialId);
                memberRepository.save(socialMember);
            }
        }
        return socialMember;
    }

    public Map<String, String> switchVendor(String vendor) {

        Map<String, String> socialUserMap = new HashMap<>();
        String clientId = "";
        String redirectUri = "";
        String clientSecret = "";
        String userInfoUri = "";
        String tokenUri = "";

        switch (vendor) {
            case "kakao":
                clientId = oAuthVendor.getKakaoClientId();
                redirectUri = oAuthVendor.getKakaoRedirectUri();
                userInfoUri = oAuthVendor.getKakaoUserInfoUri();
                tokenUri = oAuthVendor.getKakaoTokenUri();
                break;
            case "naver":
                clientId = oAuthVendor.getNaverClientId();
                redirectUri = oAuthVendor.getNaverRedirectUri();
                userInfoUri = oAuthVendor.getNaverUserInfoUri();
                tokenUri = oAuthVendor.getNaverTokenUri();
                clientSecret = oAuthVendor.getNaverClientSecret();
                break;
        }

        socialUserMap.put("clientId", clientId);
        socialUserMap.put("redirectUri", redirectUri);
        socialUserMap.put("userInfoUri", userInfoUri);
        socialUserMap.put("tokenUri", tokenUri);
        socialUserMap.put("clientSecret", clientSecret);
        return socialUserMap;
    }

    void putSocialId(String vendor, Member socialMember, String socialId){
        switch (vendor) {
            case "kakao":
                socialMember = socialMember.kakaoIdUpdate(Long.valueOf(socialId));
                break;
            case "naver":
                socialMember = socialMember.naverIdUpdate(socialId);
                break;
        }
    }

    private SignUpType getSignUpType(String vendor) {
        for (SignUpType signUpType: SignUpType.values()) {
            if (signUpType.getVendor().equals(vendor)) {
                return signUpType;
            }
        }
        throw new CustomException(NOT_MATCH_SIGNUP_TYPE);
    }

}
