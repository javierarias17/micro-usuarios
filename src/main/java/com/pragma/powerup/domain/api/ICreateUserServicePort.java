package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.UserModel;

public interface ICreateUserServicePort {
    UserModel createUser(UserModel userModel);
}
