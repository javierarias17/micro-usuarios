package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.exception.constant.FunctionalMessageConstants;
import com.pragma.powerup.domain.exception.InvalidCredentialsException;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.ITokenServicePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final ITokenServicePort tokenServicePort;

    public AuthUseCase(IUserPersistencePort userPersistencePort,
                       IPasswordEncoderPort passwordEncoderPort,
                       ITokenServicePort tokenServicePort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenServicePort = tokenServicePort;
    }

    @Override
    public String login(String email, String password) {
        UserModel user = userPersistencePort.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(
                        FunctionalMessageConstants.INVALID_CREDENTIALS));

        if (!passwordEncoderPort.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(
                    FunctionalMessageConstants.INVALID_CREDENTIALS);
        }

        return tokenServicePort.generateToken(user.getEmail(), user.getRole().getName(), user.getRole().getId(), user.getId());
    }
}
