package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICreateOwnerServicePort;
import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.NotAdultException;
import com.pragma.powerup.domain.exception.constant.FunctionalMessageConstants;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserCreateCommand;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

public class CreateOwnerUseCase implements ICreateOwnerServicePort {

    private static final int MINIMUM_OWNER_AGE = 18;

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public CreateOwnerUseCase(IUserPersistencePort userPersistencePort,
                              IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserModel createOwner(UserCreateCommand command) {
        UserModel owner = UserModel.builder()
                .name(command.name())
                .lastName(command.lastName())
                .documentNumber(command.documentNumber())
                .phone(command.phone())
                .email(command.email())
                .password(command.password() != null ? passwordEncoderPort.encode(command.password()) : null)
                .birthDate(command.birthDate())
                .role(new RoleModel(DomainConstants.OWNER_ROLE_ID, null, null))
                .build();

        validateBusinessRules(owner);
        return userPersistencePort.saveUser(owner);
    }

    private void validateBusinessRules(UserModel owner) {
        if (userPersistencePort.existsByEmail(owner.getEmail().value())) {
            throw new MailAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.EMAIL, FunctionalMessageConstants.MAIL_ALREADY_EXISTS));
        }
        if (userPersistencePort.existsByDocumentNumber(owner.getDocumentNumber().value())) {
            throw new DocumentNumberAlreadyExistsException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.DOCUMENT_NUMBER, FunctionalMessageConstants.DOCUMENT_NUMBER_ALREADY_EXISTS));
        }
        if (LocalDate.now(ZoneId.systemDefault()).minusYears(MINIMUM_OWNER_AGE)
                .isBefore(owner.getBirthDate().value())) {
            throw new NotAdultException(FunctionalMessageConstants.BUSINESS_VALIDATION_FAILED,
                    Map.of(FieldConstants.BIRTH_DATE, FunctionalMessageConstants.OWNER_NOT_OF_LEGAL_AGE));
        }
    }
}
