package com.hiro.sns.service;

import com.hiro.sns.exception.SnsApplicationException;
import com.hiro.sns.model.User;
import com.hiro.sns.model.UserEntity;
import com.hiro.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName);

        return new User();
    }

    public String login(String userName, String password) {

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(SnsApplicationException::new);

        if (userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException();
        }

        return Strings.EMPTY;
    }
}
