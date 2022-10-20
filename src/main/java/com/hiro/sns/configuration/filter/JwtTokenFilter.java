package com.hiro.sns.configuration.filter;

import com.hiro.sns.model.User;
import com.hiro.sns.service.UserService;
import com.hiro.sns.util.JwtTokenUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final String key;
	private final UserService userService;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
		throws ServletException, IOException {
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || header.startsWith("Bearer ")) {
			log.error("Error occurs while getting header. header is null or invalid");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String token = header.split(" ")[1].trim();

			if (JwtTokenUtils.isExpired(token, key)) {
				log.error("key is expired");
				return;
			}

			String userName = JwtTokenUtils.getUserName(token, key);
			User user = userService.loadUserByUserName(userName);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (RuntimeException e) {
			log.error("Error occurs while validating");
			filterChain.doFilter(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}
}
