package com.hiro.sns.exception;

import com.hiro.sns.controller.response.Response;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
		throws IOException {
		response.setContentType("application/json");
		response.setStatus(ErrorCode.INVALID_TOKEN.getHttpStatus().value());
		response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());
	}
}
