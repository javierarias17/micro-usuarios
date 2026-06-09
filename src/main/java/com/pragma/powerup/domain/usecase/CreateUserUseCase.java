package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICreateUserServicePort;
import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.util.Map;

public class CreateUserUseCase implements ICreateUserServicePort {

    private static final Long OWNER_ROLE_ID = 2L;

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public CreateUserUseCase(IUserPersistencePort userPersistencePort, IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserModel createUser(UserModel userModel) {
        validateData(userModel);
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        userModel.setRole(RoleModel.builder().id(OWNER_ROLE_ID).build());
        return userPersistencePort.saveUser(userModel);
    }

    private void validateData(UserModel userModel) {
        if (userPersistencePort.existsByEmail(userModel.getEmail())) {
            throw new MailAlreadyExistsException(ExceptionResponse.BUSINESS_VALIDATION_FAILED.getMessage(),
                    Map.of(Constants.EMAIL, ExceptionResponse.MAIL_ALREADY_EXISTS.getMessage()));
        }
        if (userPersistencePort.existsByDocumentNumber(userModel.getDocumentNumber())) {
            throw new DocumentNumberAlreadyExistsException(ExceptionResponse.BUSINESS_VALIDATION_FAILED.getMessage(),
                    Map.of(Constants.DOCUMENT_NUMBER, ExceptionResponse.DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage()));
        }
        if (LocalDate.now().minusYears(18).isBefore(userModel.getBirthDate())) {
            throw new NotAdultException(ExceptionResponse.BUSINESS_VALIDATION_FAILED.getMessage(),
                    Map.of(Constants.BIRTH_DATE, ExceptionResponse.NOT_ADULT.getMessage()));
        }
    }
}
