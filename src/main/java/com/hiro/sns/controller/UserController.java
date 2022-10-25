package com.hiro.sns.controller;

import com.hiro.sns.controller.request.UserJoinRequest;
import com.hiro.sns.controller.request.UserLoginRequest;
import com.hiro.sns.controller.response.AlarmResponse;
import com.hiro.sns.controller.response.Response;
import com.hiro.sns.controller.response.UserJoinResponse;
import com.hiro.sns.controller.response.UserLoginResponse;
import com.hiro.sns.model.User;
import com.hiro.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody final UserJoinRequest request) {
        User user = userService.join(request.getUserName(), request.getPassword());

        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserName(), request.getPassword());

        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response alarm(Pageable pageable, Authentication authentication) {
        Page<AlarmResponse> response = userService.alarmList(authentication.getName(), pageable)
            .map(AlarmResponse::fromAlarm);

        return Response.success(response);
    }
}
