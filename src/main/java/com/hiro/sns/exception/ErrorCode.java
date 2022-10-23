package com.hiro.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "user name is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),
    WRONG_PASSWORD(HttpStatus.CONFLICT, "user password is wrong"),
    USER_NOT_FOUND(HttpStatus.CONFLICT, "not found user"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "token is invalid"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "permission is invalid"), POST_NOT_FOUND(HttpStatus.CONFLICT, "not found post");

    private final HttpStatus httpStatus;
    private final String message;

}
