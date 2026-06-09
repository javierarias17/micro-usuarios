package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IUserHandler;
import com.pragma.powerup.application.mapper.IUserRequestMapper;
import com.pragma.powerup.application.mapper.IUserResponseMapper;
import com.pragma.powerup.domain.api.ICreateUserServicePort;

import com.pragma.powerup.domain.api.IValidateUserRoleServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final ICreateUserServicePort createUserServicePort;
    private final IValidateUserRoleServicePort validateUserRoleServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        return userResponseMapper.toResponse(
                createUserServicePort.createUser(userRequestMapper.toUser(userRequestDto)));
    }

    @Override
    public boolean isOwner(Long userId) {
        return validateUserRoleServicePort.isOwner(userId);
    }
}
