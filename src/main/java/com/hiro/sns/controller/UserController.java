package com.hiro.sns.controller;

import com.hiro.sns.controller.request.UserJoinRequest;
import com.hiro.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void join(final UserJoinRequest request) {
        userService.join("", "");
    }
}
