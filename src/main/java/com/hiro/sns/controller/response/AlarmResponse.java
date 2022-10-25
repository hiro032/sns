package com.hiro.sns.controller.response;

import com.hiro.sns.model.Alarm;
import com.hiro.sns.model.AlarmArgs;
import com.hiro.sns.model.AlarmType;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmResponse {
	private Integer id;

	private AlarmType alarmType;

	private AlarmArgs alarmArgs;

	private String text;

	private Timestamp registeredAt;

	private Timestamp updatedAt;

	private Timestamp deletedAt;

	public static AlarmResponse fromAlarm(Alarm alarm) {
		return new AlarmResponse(
			alarm.getId(),
			alarm.getAlarmType(),
			alarm.getArgs(),
			alarm.getAlarmType().getAlarmText(),
			alarm.getRegisteredAt(),
			alarm.getUpdatedAt(),
			alarm.getDeletedAt()
		);
	}
}
