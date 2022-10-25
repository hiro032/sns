package com.hiro.sns.repository;

import com.hiro.sns.model.entity.AlarmEntity;
import com.hiro.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

	Page<AlarmEntity> findAllByUser(UserEntity user);

}
