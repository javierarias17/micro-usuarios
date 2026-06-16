package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICreateCustomerServicePort;
import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.constant.FunctionalMessageConstants;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.validator.UserValidator;

import java.util.Map;

public class CreateCustomerUseCase implements ICreateCustomerServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final UserValidator userValidator;

    public CreateCustomerUseCase(IUserPersistencePort userPersistencePort,
                                 IPasswordEncoderPort passwordEncoderPort,
                                 UserValidator userValidator) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.userValidator = userValidator;
    }

    @Override
    public UserModel createCustomer(UserModel userModel) {
        userValidator.validate(userModel);
        validateBusinessRules(userModel);
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        userModel.setRole(RoleModel.builder().id(DomainConstants.CUSTOMER_ROLE_ID).build());
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
    }
}
