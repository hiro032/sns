package com.hiro.sns.repository;

import com.hiro.sns.model.Post;
import com.hiro.sns.model.entity.PostEntity;
import com.hiro.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {

	Page<Post> findAllByUser(UserEntity userEntity, Pageable pageable);

}
