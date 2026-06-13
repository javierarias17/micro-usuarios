package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.UserModel;

public interface ICreateCustomerServicePort {
    UserModel createCustomer(UserModel userModel);
}
