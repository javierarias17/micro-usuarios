package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;

public interface IUserHandler {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    boolean isOwner(Long userId);
}
