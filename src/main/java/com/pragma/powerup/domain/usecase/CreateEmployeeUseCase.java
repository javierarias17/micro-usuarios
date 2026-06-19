package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICreateEmployeeServicePort;
import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.ForbiddenException;
import com.pragma.powerup.domain.exception.constant.FunctionalMessageConstants;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserCreateCommand;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IPlazoletaServicePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.util.Map;

public class CreateEmployeeUseCase implements ICreateEmployeeServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final IPlazoletaServicePort plazoletaServicePort;

    public CreateEmployeeUseCase(IUserPersistencePort userPersistencePort,
            IPasswordEncoderPort passwordEncoderPort,
            IPlazoletaServicePort plazoletaServicePort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.plazoletaServicePort = plazoletaServicePort;
    }

    @Override
    public UserModel createEmployee(UserCreateCommand command) {

        UserModel employee = UserModel.builder()
                .name(command.name())
                .lastName(command.lastName())
                .documentNumber(command.documentNumber())
                .phone(command.phone())
                .email(command.email())
                .password(command.password() != null ? passwordEncoderPort.encode(command.password()) : null)
                .birthDate(command.birthDate())
                .role(new RoleModel(DomainConstants.EMPLOYEE_ROLE_ID, null, null))
                .restaurantId(command.restaurantId())
                .build();

        this.validateBusinessRules(employee);
        return userPersistencePort.saveUser(employee);
    }

    private void validateBusinessRules(UserModel employee) {
        if (!plazoletaServicePort.isOwnerOfRestaurant(employee.getRestaurantId().value())) {
            throw new ForbiddenException();
        }

        if (userPersistencePort.existsByEmail(employee.getEmail().value())) {
            throw new MailAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.EMAIL, FunctionalMessageConstants.MAIL_ALREADY_EXISTS));
        }
        if (userPersistencePort.existsByDocumentNumber(employee.getDocumentNumber().value())) {
            throw new DocumentNumberAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.DOCUMENT_NUMBER, FunctionalMessageConstants.DOCUMENT_NUMBER_ALREADY_EXISTS));
        }
    }
}
