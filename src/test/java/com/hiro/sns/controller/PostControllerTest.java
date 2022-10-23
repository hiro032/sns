package com.hiro.sns.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiro.sns.controller.request.PostCreateRequest;
import com.hiro.sns.controller.request.PostModifyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("포스트 작성 성공")
	@Test
	void write_post() throws Exception {
		String title = "title";
		String body = "body";

		mockMvc.perform(post("/api/v1/posts")
			.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
			.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	void modify_post() throws Exception {
		String title = "title";
		String body = "body";

		mockMvc.perform(put("/api/v1/posts/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
