package com.seungah.todayclothes.common.jwt;

import static com.seungah.todayclothes.common.exception.ErrorCode.ACCESS_DENIED;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(response.SC_FORBIDDEN);

		JSONObject responseObject = new JSONObject();
		responseObject.put("errorCode", ACCESS_DENIED.getCode());
		responseObject.put("errorMessage", ACCESS_DENIED.getMessage());

		response.getWriter().print(responseObject);

	}
}
