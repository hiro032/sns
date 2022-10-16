package com.hiro.sns.controller;

import com.hiro.sns.controller.request.UserJoinRequest;
import com.hiro.sns.controller.response.Response;
import com.hiro.sns.controller.response.UserJoinResponse;
import com.hiro.sns.model.User;
import com.hiro.sns.service.UserService;
import lombok.RequiredArgsConstructor;
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
}
