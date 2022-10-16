package com.hiro.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiro.sns.controller.request.UserJoinRequest;
import com.hiro.sns.controller.request.UserLoginRequest;
import com.hiro.sns.exception.ErrorCode;
import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.model.User;
import com.hiro.sns.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @DisplayName("회원가입")
    @Test
    void sign_in() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.join("", ""))
                .thenReturn(mock(User.class));

        mockMvc.perform(post("/api/vi/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))

        ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입 실패: 이미 등록된 userName 으로 회원 가입 시도")
    @Test
    void fail_sign_in() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.join("", ""))
                .thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/vi/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.login(userName, password))
                .thenReturn("test_token");

        mockMvc.perform(post("/api/vi/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("로그인 실패: 회원가입 안된 userName 으로 로그인 시도")
    @Test
    void fail_login_not_found_user_name() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.login(userName, password))
                .thenReturn("test_token");

        mockMvc.perform(post("/api/vi/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("로그인 실패: 틀린 비밀번호 입력")
    @Test
    void fail_login_wrong_password() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.login(userName, password))
                .thenReturn("test_token");

        mockMvc.perform(post("/api/vi/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}