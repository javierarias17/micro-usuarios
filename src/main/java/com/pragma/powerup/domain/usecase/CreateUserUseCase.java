package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.NotAdultException;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.time.LocalDate;

public class CreateUserUseCase implements IUserServicePort {

    private static final Long OWNER_ROLE_ID = 2L;

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public CreateUserUseCase(IUserPersistencePort userPersistencePort, IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void createUser(UserModel userModel) {
        if (userPersistencePort.existsByEmail(userModel.getEmail())) {
            throw new MailAlreadyExistsException();
        }
        if (userPersistencePort.existsByDocumentNumber(userModel.getDocumentNumber())) {
            throw new DocumentNumberAlreadyExistsException();
        }
        if (!isAdult(userModel.getBirthDate())) {
            throw new NotAdultException();
        }
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        userModel.setRole(userPersistencePort.getRoleById(OWNER_ROLE_ID));
        userPersistencePort.saveUser(userModel);
    }

    private boolean isAdult(LocalDate birthDate) {
        return !LocalDate.now().minusYears(18).isBefore(birthDate);
    }
}
