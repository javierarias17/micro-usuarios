package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICreateOwnerServicePort;
import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.exception.constant.FunctionalMessageConstants;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.validator.UserValidator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

public class CreateOwnerUseCase implements ICreateOwnerServicePort {

    private static final int MINIMUM_OWNER_AGE = 18;

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final UserValidator userValidator;

    public CreateOwnerUseCase(IUserPersistencePort userPersistencePort,
                              IPasswordEncoderPort passwordEncoderPort,
                              UserValidator userValidator) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.userValidator = userValidator;
    }

    @Override
    public UserModel createOwner(UserModel userModel) {
        userValidator.validate(userModel);
        validateBusinessRules(userModel);
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        userModel.setRole(RoleModel.builder().id(DomainConstants.OWNER_ROLE_ID).build());
        return userPersistencePort.saveUser(userModel);
    }

    private void validateBusinessRules(UserModel userModel) {
        if (userPersistencePort.existsByEmail(userModel.getEmail())) {
            throw new MailAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.EMAIL, FunctionalMessageConstants.MAIL_ALREADY_EXISTS));
        }
        if (userPersistencePort.existsByDocumentNumber(userModel.getDocumentNumber())) {
            throw new DocumentNumberAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.DOCUMENT_NUMBER, FunctionalMessageConstants.DOCUMENT_NUMBER_ALREADY_EXISTS));
        }
        if (LocalDate.now(ZoneId.systemDefault()).minusYears(MINIMUM_OWNER_AGE).isBefore(userModel.getBirthDate())) {
            throw new NotAdultException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.BIRTH_DATE, FunctionalMessageConstants.OWNER_NOT_OF_LEGAL_AGE));
        }
    }
}
