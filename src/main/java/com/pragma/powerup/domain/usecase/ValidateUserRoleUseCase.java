package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.api.IValidateUserRoleServicePort;
import com.pragma.powerup.domain.exception.UserNotFoundException;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

public class ValidateUserRoleUseCase implements IValidateUserRoleServicePort {

    private final IUserPersistencePort userPersistencePort;

    public ValidateUserRoleUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public boolean isOwner(Long userId) {
        UserModel user = userPersistencePort.getUserById(userId)
                .orElseThrow(UserNotFoundException::new);
        return user.getRole() != null && DomainConstants.OWNER_ROLE_ID.equals(user.getRole().getId());
    }

}
