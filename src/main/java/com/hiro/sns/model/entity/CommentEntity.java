package com.hiro.sns.model.entity;

import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "\"comment\"", indexes = {
	@Index(name = "post_id_idx", columnList = "post_id")
})
@Data
@SQLDelete(sql = "UPDATE \"comment\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class CommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private PostEntity post;

	@Column(name = "comment")
	private String comment;

	@Column(name = "register_at")
	private Timestamp registeredAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "deleted_at")
	private Timestamp deletedAt;

	@PrePersist
	void registeredAt() {
		this.registeredAt = Timestamp.from(Instant.now());
	}

	@PreUpdate
	void updateAt() {
		this.updatedAt = Timestamp.from(Instant.now());
	}

	public static CommentEntity of(UserEntity user, PostEntity post, String comment) {
		CommentEntity commentEntity = new CommentEntity();
		commentEntity.setUser(user);
		commentEntity.setPost(post);
		commentEntity.setComment(comment);
		return commentEntity;
	}
}
