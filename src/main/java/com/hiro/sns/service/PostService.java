package com.hiro.sns.service;

import com.hiro.sns.exception.ErrorCode;
import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.model.Post;
import com.hiro.sns.model.entity.PostEntity;
import com.hiro.sns.model.entity.UserEntity;
import com.hiro.sns.repository.PostEntityRepository;
import com.hiro.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostEntityRepository postEntityRepository;
	private final UserEntityRepository userEntityRepository;

	@Transactional
	public Post create(String title, String body, String userName) {
		final UserEntity userEntity = userEntityRepository.findByUserName(userName)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

		final PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));

		return Post.fromEntity(saved);
	}

	@Transactional
	public Post modify(String title, String body, String userName, Integer postId) {
		final UserEntity userEntity = userEntityRepository.findByUserName(userName)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

		final PostEntity postEntity = postEntityRepository.findById(postId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", userName)));

		if (postEntity.getUser() != userEntity) {
			throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission", userName));
		}

		postEntity.setTitle(title);
		postEntity.setBody(body);

		return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
	}

	@Transactional
	public void delete(String title, String body, String userName, Integer postId) {
		final UserEntity userEntity = userEntityRepository.findByUserName(userName)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

		final PostEntity postEntity = postEntityRepository.findById(postId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", userName)));

		if (postEntity.getUser() != userEntity) {
			throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission", userName));
		}

		postEntityRepository.delete(postEntity);
	}
}
