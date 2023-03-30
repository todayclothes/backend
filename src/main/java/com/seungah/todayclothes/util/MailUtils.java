package com.seungah.todayclothes.util;

import static com.seungah.todayclothes.common.exception.ErrorCode.FAILED_SEND_MAIL;

import com.seungah.todayclothes.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailUtils {

	private final JavaMailSender javaMailSender;

	public void sendMail(String mail, String subject, String text) {
		try {
			MimeMessagePreparator message = mimeMessage -> {
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true,
					"UTF-8");
				mimeMessageHelper.setTo(mail);
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(text, true);
			};

			javaMailSender.send(message);
		} catch (MailException e) {
			log.error(e.getMessage());
			throw new CustomException(FAILED_SEND_MAIL);
		}

	}

}
