package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.UserModel;

public interface ICreateOwnerServicePort {
    UserModel createOwner(UserModel userModel);
}
