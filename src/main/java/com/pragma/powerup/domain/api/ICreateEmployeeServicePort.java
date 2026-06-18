package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.UserCreateCommand;
import com.pragma.powerup.domain.model.UserModel;

public interface ICreateEmployeeServicePort {
    UserModel createEmployee(UserCreateCommand command);
}
