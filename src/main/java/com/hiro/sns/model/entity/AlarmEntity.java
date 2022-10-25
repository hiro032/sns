package com.hiro.sns.model.entity;

import com.hiro.sns.model.AlarmArgs;
import com.hiro.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "\"alarm\"", indexes = {
	@Index(name = "user_id_idx", columnList = "user_id")
})
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Enumerated(EnumType.STRING)
	private AlarmType alarmType;

	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	private AlarmArgs args;

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

	public static AlarmEntity of(UserEntity user, AlarmType alarmType, AlarmArgs args) {
		AlarmEntity alarmEntity = new AlarmEntity();
		alarmEntity.setUser(user);
		alarmEntity.setAlarmType(alarmType);
		alarmEntity.setArgs(args);
		return alarmEntity;
	}
}
