package com.hiro.sns.service;

import com.hiro.sns.exception.ErrorCode;
import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.model.User;
import com.hiro.sns.model.entity.UserEntity;
import com.hiro.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public User join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("userName is %s", userName));
        });

        UserEntity savedUser = userRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return User.fromEntity(savedUser);
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.WRONG_PASSWORD, "");
        }

        return Strings.EMPTY;
    }
}
