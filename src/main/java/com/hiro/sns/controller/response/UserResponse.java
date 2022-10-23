package com.hiro.sns.controller.response;

import com.hiro.sns.model.User;
import com.hiro.sns.model.UserRole;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private Integer id;

	private String userName;

	private UserRole userRole;

	private Timestamp registeredAt;

	private Timestamp updatedAt;

	private Timestamp deletedAt;

	public static UserResponse fromUser(final User user) {
		return new UserResponse(
			user.getId(),
			user.getUsername(),
			user.getUserRole(),
			user.getRegisteredAt(),
			user.getUpdatedAt(),
			user.getDeletedAt()
		);
	}
}
