package com.hiro.sns.service;

import com.hiro.sns.exception.ErrorCode;
import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.model.User;
import com.hiro.sns.model.entity.UserEntity;
import com.hiro.sns.repository.UserEntityRepository;
import com.hiro.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
    public User join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("userName is %s", userName));
        });

        UserEntity savedUser = userRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return User.fromEntity(savedUser);
    }

    public User loadUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
            .map(User::fromEntity)
            .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, "user not found"));
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        if (!encoder.matches(userEntity.getPassword(), encoder.encode(password))) {
            throw new SnsApplicationException(ErrorCode.WRONG_PASSWORD, "");
        }

        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }
}
