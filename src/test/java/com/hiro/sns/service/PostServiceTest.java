package com.hiro.sns.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hiro.sns.exception.ErrorCode;
import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.model.entity.PostEntity;
import com.hiro.sns.model.entity.UserEntity;
import com.hiro.sns.repository.PostEntityRepository;
import com.hiro.sns.repository.UserEntityRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PostServiceTest {

	@Autowired
	private PostService postService;

	@MockBean
	private PostEntityRepository postEntityRepository;

	@MockBean
	private UserEntityRepository userEntityRepository;

	@DisplayName("포스트 작성")
	@Test
	void create_post() {
		String title = "title";
		String body = "body";
		String userName = "userName";

		when(userEntityRepository.findByUserName(userName))
			.thenReturn(Optional.of(mock(UserEntity.class)));

		when(postEntityRepository.save(any()))
			.thenReturn(mock(PostEntity.class));

		assertDoesNotThrow(() -> postService.create(title, body, userName));
	}

	@DisplayName("포스트 작성 실패: 요청한 유저가 없는 경우")
	@Test
	void fail_create_post() {
		String title = "title";
		String body = "body";
		String userName = "userName";

		when(userEntityRepository.findByUserName(userName))
			.thenReturn(Optional.empty());

		when(postEntityRepository.save(any()))
			.thenReturn(mock(PostEntity.class));

		SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
		assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
	}
}
