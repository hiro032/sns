package com.hiro.sns.controller.response;

import com.hiro.sns.model.Comment;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
	private Integer id;

	private String comment;

	private String userName;

	private Integer postId;

	private Timestamp registeredAt;

	private Timestamp updatedAt;

	private Timestamp removedAt;

	private static CommentResponse fromEntity(Comment comment) {
		return new CommentResponse(
			comment.getId(),
			comment.getComment(),
			comment.getUserName(),
			comment.getId(),
			comment.getRegisteredAt(),
			comment.getUpdatedAt(),
			comment.getDeletedAt()
		);
	}
}
