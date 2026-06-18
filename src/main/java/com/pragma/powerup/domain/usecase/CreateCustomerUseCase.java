package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICreateCustomerServicePort;
import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.constant.FunctionalMessageConstants;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserCreateCommand;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.util.Map;

public class CreateCustomerUseCase implements ICreateCustomerServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public CreateCustomerUseCase(IUserPersistencePort userPersistencePort,
            IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserModel createCustomer(UserCreateCommand command) {
        UserModel customer = UserModel.builder()
                .name(command.name())
                .lastName(command.lastName())
                .documentNumber(command.documentNumber())
                .phone(command.phone())
                .email(command.email())
                .password(command.password() != null ? passwordEncoderPort.encode(command.password()) : null)
                .birthDate(command.birthDate())
                .role(new RoleModel(DomainConstants.CUSTOMER_ROLE_ID, null, null))
                .build();

        validateBusinessRules(customer);
        return userPersistencePort.saveUser(customer);
    }

    private void validateBusinessRules(UserModel customer) {
        if (userPersistencePort.existsByEmail(customer.getEmail().value())) {
            throw new MailAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.EMAIL, FunctionalMessageConstants.MAIL_ALREADY_EXISTS));
        }
        if (userPersistencePort.existsByDocumentNumber(customer.getDocumentNumber().value())) {
            throw new DocumentNumberAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.DOCUMENT_NUMBER, FunctionalMessageConstants.DOCUMENT_NUMBER_ALREADY_EXISTS));
        }
    }
}
