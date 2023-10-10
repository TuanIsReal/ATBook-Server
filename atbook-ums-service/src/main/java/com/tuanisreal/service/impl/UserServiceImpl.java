package com.tuanisreal.service.impl;

import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.context.user.domain.User;
import com.tuanisreal.context.user.request.LoginUserRequest;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.repository.base.UserRepository;
import com.tuanisreal.repository.base.UserSaltRepository;
import com.tuanisreal.service.base.UserService;
import com.tuanisreal.utils.DateTimeUtil;
import com.tuanisreal.utils.PasswordUtil;
import com.tuanisreal.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserSaltRepository userSaltRepository;

    @Override
    public User login(LoginUserRequest loginUserRequest) {
        if (Objects.isNull(loginUserRequest.getEmail()) || Objects.isNull(loginUserRequest.getPassword())){
            throw new ApplicationException(ResponseCode.WRONG_DATA_FORMAT);
        }
        User user = userRepository.getUserByEmail(loginUserRequest.getEmail());
        if (Objects.isNull(user)){
            throw new ApplicationException(ResponseCode.EMAIL_NOT_FOUND);
        }
        String salt = userSaltRepository.getSalt(user.getUserId());
        if (isCorrectPassword(loginUserRequest.getPassword(), user.getPassword(), salt)){
            throw new ApplicationException(ResponseCode.INCORRECT_PASSWORD);
        }
        return user;
    }

    @Override
    public User insertUser(User user) {
        String salt = PasswordUtil.generateSalt();
        String email = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        password = PasswordUtil.encryptPassword(password, salt);
        user.setEmail(email);
        user.setPassword(password);
        user.setLastUpdate(DateTimeUtil.currentTime());
        user = userRepository.insertUser(user);
        userRepository.updateUserId(user.getUserId());
        userSaltRepository.insertUserSalt(user.getUserId(), salt);
        return user;
    }

    @Override
    public boolean changeEmailPassword(String userId, String email, String oldPassword, String newPassword) {
        if (Objects.isNull(email) || Objects.isNull(oldPassword) || Objects.isNull(newPassword) || oldPassword.equals(newPassword)){
            throw new ApplicationException(ResponseCode.WRONG_DATA_FORMAT);
        }

        if (newPassword.length() < 6){
            throw new ApplicationException(ResponseCode.INVALID_PASSWORD);
        }

        if (userRepository.checkEmailVersion(email)){
            throw new ApplicationException(ResponseCode.EMAIL_REGISTERED);
        }

        User user = userRepository.getUserByUserId(userId);
        checkUserExistAndRegisterComplete(user);

        String salt = userSaltRepository.getSalt(user.getUserId());
        if (isCorrectPassword(oldPassword, user.getPassword(), salt)){
            throw new ApplicationException(ResponseCode.INCORRECT_PASSWORD);
        }

        email = email.toLowerCase();
        String encryptedNewPassword;
        encryptedNewPassword = PasswordUtil.encryptPassword(newPassword, salt);
        userRepository.changeEmailPassword(userId, email, encryptedNewPassword, DateTimeUtil.currentTime());
        user = userRepository.getUserByUserId(userId);
        return user.getIsFinishRegister();
    }

    private boolean isCorrectPassword(String pwdRequest, String encryptedPwdUser, String salt) {
        if (StringUtil.validateString(pwdRequest)) {
            String encryptedPassRequest = PasswordUtil.encryptPassword(pwdRequest, salt);
            return encryptedPwdUser.equals(encryptedPassRequest);
        }
        return false;
    }

    private void checkUserExistAndRegisterComplete(User user) {
        if (Objects.isNull(user)){
            throw new ApplicationException(ResponseCode.USER_NOT_EXIST);
        }
        if (user.getIsFinishRegister()){
            throw new ApplicationException(ResponseCode.USER_FINISHED_REGISTER);
        }
    }
}
