package com.seungah.todayclothes.global.sms;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsDto {
	private String type;
	private String contentType;
	private String countryCode;
	private String from;
	private String content;
	private List<Message> messages;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Message {

		private String to;
		private String content;

		public static Message from(String from, String content) {
			return Message.builder()
				.to(from)
				.content("[오늘 뭐 입지?] " + content)
				.build();
		}
	}

	public static SmsDto from(String from, String phone, List<Message> messages) {
		return SmsDto.builder()
			.type("SMS")
			.contentType("COMM")
			.countryCode("82")
			.from(phone)
			.content("content") // TODO 여러개 보낼 메시지가 있는지 토의
			.messages(messages)
			.build();
	}
}