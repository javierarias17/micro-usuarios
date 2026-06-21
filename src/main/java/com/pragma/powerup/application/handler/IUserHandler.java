package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.CustomerRequestDto;
import com.pragma.powerup.application.dto.request.EmployeeRequestDto;
import com.pragma.powerup.application.dto.request.OwnerRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;

public interface IUserHandler {
    UserResponseDto createOwner(OwnerRequestDto ownerRequestDto);
    UserResponseDto createEmployee(EmployeeRequestDto employeeRequestDto);
    UserResponseDto createCustomer(CustomerRequestDto customerRequestDto);
    boolean isOwner(Long userId);
    Long getRestaurantIdByEmployeeId(Long employeeId);
    String getPhoneByCustomerId(Long customerId);
}
