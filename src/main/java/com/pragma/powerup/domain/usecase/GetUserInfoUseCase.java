package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IGetUserInfoServicePort;
import com.pragma.powerup.domain.exception.NotFoundException;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

public class GetUserInfoUseCase implements IGetUserInfoServicePort {

    private final IUserPersistencePort userPersistencePort;

    public GetUserInfoUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public Long getRestaurantIdByEmployeeId(Long employeeId) {
        return userPersistencePort.findRestaurantIdByEmployeeId(employeeId)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public String getPhoneByCustomerId(Long customerId) {
        return userPersistencePort.findPhoneByCustomerId(customerId)
                .orElseThrow(NotFoundException::new);
    }
}