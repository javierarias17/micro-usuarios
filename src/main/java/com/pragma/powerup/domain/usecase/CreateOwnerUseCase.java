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

import java.time.LocalDate;
import java.util.Map;

public class CreateOwnerUseCase implements ICreateOwnerServicePort {

    private static final int MINIMUM_OWNER_AGE = 18;

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public CreateOwnerUseCase(IUserPersistencePort userPersistencePort, IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserModel createOwner(UserModel userModel) {
        validateData(userModel);
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        userModel.setRole(RoleModel.builder().id(DomainConstants.OWNER_ROLE_ID).build());
        return userPersistencePort.saveUser(userModel);
    }

    private void validateData(UserModel userModel) {
        if (userPersistencePort.existsByEmail(userModel.getEmail())) {
            throw new MailAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.EMAIL, FunctionalMessageConstants.MAIL_ALREADY_EXISTS));
        }
        if (userPersistencePort.existsByDocumentNumber(userModel.getDocumentNumber())) {
            throw new DocumentNumberAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.DOCUMENT_NUMBER, FunctionalMessageConstants.DOCUMENT_NUMBER_ALREADY_EXISTS));
        }
        if (LocalDate.now().minusYears(MINIMUM_OWNER_AGE).isBefore(userModel.getBirthDate())) {
            throw new NotAdultException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.BIRTH_DATE, FunctionalMessageConstants.OWNER_NOT_OF_LEGAL_AGE));
        }
    }
}
