package com.hiro.sns.service;

import com.hiro.sns.exception.ErrorCode;
import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.model.Post;
import com.hiro.sns.model.entity.CommentEntity;
import com.hiro.sns.model.entity.LikeEntity;
import com.hiro.sns.model.entity.PostEntity;
import com.hiro.sns.model.entity.UserEntity;
import com.hiro.sns.repository.CommentEntityRepository;
import com.hiro.sns.repository.LikeEntityRepository;
import com.hiro.sns.repository.PostEntityRepository;
import com.hiro.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice.AssignReturned.ToReturned;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostEntityRepository postEntityRepository;
	private final UserEntityRepository userEntityRepository;
	private final LikeEntityRepository likeEntityRepository;
	private final CommentEntityRepository commentEntityRepository;

	@Transactional
	public Post create(String title, String body, String userName) {
		UserEntity userEntity = getUserOrException(userName);

		PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));

		return Post.fromEntity(saved);
	}

	@Transactional
	public Post modify(String title, String body, String userName, Integer postId) {
		UserEntity userEntity = getUserOrException(userName);

		PostEntity postEntity = getPostOrException(postId);

		if (postEntity.getUser() != userEntity) {
			throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission", userName));
		}

		postEntity.setTitle(title);
		postEntity.setBody(body);

		return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
	}

	@Transactional
	public void delete(String title, String body, String userName, Integer postId) {
		UserEntity userEntity = getUserOrException(userName);
		PostEntity postEntity = getPostOrException(postId);

		if (postEntity.getUser() != userEntity) {
			throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission", userName));
		}

		postEntityRepository.delete(postEntity);
	}

	public Page<Post> list(Pageable pageable) {
		return postEntityRepository.findAll(pageable)
			.map(Post::fromEntity);
	}

	public Page<Post> my(String userName, Pageable pageable) {
		UserEntity userEntity = getUserOrException(userName);

		return postEntityRepository.findAllByUser(userEntity, pageable);
	}

	public void like(Integer postId, String userName) {
		UserEntity userEntity = getUserOrException(userName);
		PostEntity postEntity = getPostOrException(postId);

		likeEntityRepository.findByUserAndPost(userEntity, postEntity)
			.ifPresent(it -> {
				throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("username %s already like post %d", userName, postEntity.getId()));
			});

		likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
	}

	public int likeCount(Integer postId) {
		PostEntity postEntity = getPostOrException(postId);

		return likeEntityRepository.countByPost(postEntity);
	}

	@ToReturned
	public void comment(Integer postId, String userName, String comment) {
		UserEntity userEntity = getUserOrException(userName);
		PostEntity postEntity = getPostOrException(postId);

		commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));
	}

	public Page<CommentEntity> getComments(Integer postId, Pageable pageable) {
		PostEntity postEntity = getPostOrException(postId);

		return commentEntityRepository.findAllByPost(postEntity, pageable);
	}

	private PostEntity getPostOrException(Integer postId) {
		return postEntityRepository.findById(postId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%d not found", postId)));
	}

	private UserEntity getUserOrException(String userName) {
		return userEntityRepository.findByUserName(userName)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
	}

}
