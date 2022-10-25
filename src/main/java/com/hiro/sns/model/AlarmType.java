package com.hiro.sns.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {

	NEW_CONTENT_ON_POST("new comment"),
	NEW_LIKE_ON_POST("new like");

	private final String alarmText;

}
