package com.seungah.todayclothes.global.jwt;

import static com.seungah.todayclothes.global.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static com.seungah.todayclothes.global.exception.ErrorCode.INVALID_AUTH;
import static com.seungah.todayclothes.global.exception.ErrorCode.INVALID_TOKEN;

import com.seungah.todayclothes.global.exception.ErrorCode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		String exception = (String)request.getAttribute("exception");

		if (exception.equals(EXPIRED_ACCESS_TOKEN.getCode())) {
			setResponse(response, EXPIRED_ACCESS_TOKEN);
		} else if (exception.equals(INVALID_TOKEN.getCode())) {
			setResponse(response, INVALID_TOKEN);
		} else {
			setResponse(response, INVALID_AUTH);
		}
	}

	private void setResponse(HttpServletResponse response, ErrorCode errorCode)
		throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		JSONObject responseObject = new JSONObject();
		responseObject.put("errorCode", errorCode.getCode());
		responseObject.put("errorMessage", errorCode.getMessage());

		response.getWriter().print(responseObject);

	}
}
