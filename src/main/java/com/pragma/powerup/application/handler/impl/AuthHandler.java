package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.AuthRequestDto;
import com.pragma.powerup.application.dto.response.AuthResponseDto;
import com.pragma.powerup.application.handler.IAuthHandler;
import com.pragma.powerup.domain.api.IAuthServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthHandler implements IAuthHandler {

    private final IAuthServicePort authServicePort;

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        String token = authServicePort.login(authRequestDto.getEmail(), authRequestDto.getUserPassword());
        return new AuthResponseDto(token);
    }
}
