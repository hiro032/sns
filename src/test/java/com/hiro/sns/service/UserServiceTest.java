package com.hiro.sns.service;

import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.fixture.UserEntityFixture;
import com.hiro.sns.model.UserEntity;
import com.hiro.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @DisplayName("회원 가입")
    @Test
    void join() {
        String userName = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(userName))
                .thenReturn(Optional.empty());

        when(userEntityRepository.save(any()))
                .thenReturn(Optional.of(mock(UserEntity.class)));


        assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @DisplayName("로그인 성공")
    @Test
    void login() {
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName))
                .thenReturn(Optional.of(fixture));

        when(userEntityRepository.save(any()))
                .thenReturn(Optional.of(fixture));

        assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @DisplayName("로그인 실패: userName 에 해당하는 회원이 없는 경우")
    @Test
    void fail_login_not_found_user_name() {
        String userName = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(userName))
                .thenReturn(Optional.empty());

        assertThrows(SnsApplicationException.class,
                () -> userService.login(userName, password));
    }

    @DisplayName("로그인 실패: password not matching")
    @Test
    void fail_login_password_matching() {
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName))
                .thenReturn(Optional.of(fixture));

        assertThrows(SnsApplicationException.class,
                () -> userService.login(userName, password));
    }

    @DisplayName("회원 가입 실패: 중복된 userName")
    @Test
    void fail_join_duplicate_user_name() {
        String userName = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(userName))
                .thenReturn(Optional.of(mock(UserEntity.class)));

        when(userEntityRepository.save(any()))
                .thenReturn(Optional.of(mock(UserEntity.class)));


        assertThrows(SnsApplicationException.class,
                () -> userService.join(userName, password));
    }
}