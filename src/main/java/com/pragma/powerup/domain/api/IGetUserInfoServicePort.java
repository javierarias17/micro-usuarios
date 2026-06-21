package com.pragma.powerup.domain.api;

public interface IGetUserInfoServicePort {
    Long getRestaurantIdByEmployeeId(Long employeeId);
    String getPhoneByCustomerId(Long customerId);
}
