package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CustomerRequestDto;
import com.pragma.powerup.application.dto.request.EmployeeRequestDto;
import com.pragma.powerup.application.dto.request.OwnerRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IUserHandler;
import com.pragma.powerup.application.mapper.IUserRequestMapper;
import com.pragma.powerup.application.mapper.IUserResponseMapper;
import com.pragma.powerup.domain.api.ICreateCustomerServicePort;
import com.pragma.powerup.domain.api.ICreateEmployeeServicePort;
import com.pragma.powerup.domain.api.ICreateOwnerServicePort;
import com.pragma.powerup.domain.api.IValidateUserRoleServicePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final ICreateOwnerServicePort createOwnerServicePort;
    private final ICreateEmployeeServicePort createEmployeeServicePort;
    private final ICreateCustomerServicePort createCustomerServicePort;
    private final IValidateUserRoleServicePort validateUserRoleServicePort;
    private final IUserPersistencePort userPersistencePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;

    @Override
    public UserResponseDto createOwner(OwnerRequestDto ownerRequestDto) {
        return userResponseMapper.toResponse(
                createOwnerServicePort.createOwner(userRequestMapper.toUserFromOwner(ownerRequestDto)));
    }

    @Override
    public UserResponseDto createEmployee(EmployeeRequestDto employeeRequestDto) {
        return userResponseMapper.toResponse(
                createEmployeeServicePort.createEmployee(
                        userRequestMapper.toUserFromEmployee(employeeRequestDto)));
    }

    @Override
    public UserResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        return userResponseMapper.toResponse(
                createCustomerServicePort.createCustomer(userRequestMapper.toUserFromCustomer(customerRequestDto)));
    }

    @Override
    public boolean isOwner(Long userId) {
        return validateUserRoleServicePort.isOwner(userId);
    }

    @Override
    public Long getRestaurantIdByEmployeeId(Long employeeId) {
        return userPersistencePort.getUserById(employeeId)
                .map(user -> user.getRestaurantId() != null ? user.getRestaurantId().value() : null)
                .orElse(null);
    }
}
