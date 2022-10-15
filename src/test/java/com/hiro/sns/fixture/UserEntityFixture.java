package com.hiro.sns.fixture;

import com.hiro.sns.model.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        return userEntity;
    }
}