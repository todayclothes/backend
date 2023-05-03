package com.seungah.todayclothes.global.sms;

import static com.seungah.todayclothes.global.exception.ErrorCode.FAILED_SEND_SMS;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.sms.SmsDto.Message;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class SmsUtils {

	@Value("${naver.service-id}")
	private String serviceId;
	@Value("${naver.access-key}")
	private String accessKey;
	@Value("${naver.secret-key}")
	private String secretKey;
	@Value("${naver.phone}")
	private String phone;

	public void sendSms(String from, String content) {
		String timestamp = Long.toString(System.currentTimeMillis());

		URI apiUri = UriComponentsBuilder.newInstance()
			.scheme("https").host("sens.apigw.ntruss.com")
			.path("/sms/v2/services/{serviceId}/messages")
			.buildAndExpand(serviceId).toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", timestamp);
		headers.set("x-ncp-iam-access-key", accessKey);
		headers.set("x-ncp-apigw-signature-v2", getSignature(timestamp));

		SmsDto smsDto = getSmsDto(from, content);

		ObjectMapper objectMapper = new ObjectMapper();
		String body = null;
		try {
			body = objectMapper.writeValueAsString(smsDto);
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
			throw new CustomException(FAILED_SEND_SMS);
		}
		var request = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(
			apiUri, HttpMethod.POST, request, String.class);

		int responseCode = response.getStatusCodeValue();
		if (responseCode != 202) {
			log.info(String.valueOf(responseCode));
			throw new CustomException(FAILED_SEND_SMS);
		}
	}

	private SmsDto getSmsDto(String from, String content) {
		Message message = Message.from(from, content);
		List<Message> messages = new ArrayList<>();
		messages.add(message);
		return SmsDto.from(from, phone, messages);
	}

	private String getSignature(String time) {
		String space = " ";
		String newLine = "\n";
		String method = "POST";
		String url = "/sms/v2/services/" + serviceId + "/messages";

		String message = method + space + url
			+ newLine + time + newLine + accessKey;

		SecretKeySpec signingKey = new SecretKeySpec(
			secretKey.getBytes(UTF_8), "HmacSHA256");
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			log.info(e.getMessage());
			throw new CustomException(FAILED_SEND_SMS);
		}

		byte[] rawHmac = mac.doFinal(message.getBytes(UTF_8));

		return Base64.getEncoder().encodeToString(rawHmac);
	}

}
