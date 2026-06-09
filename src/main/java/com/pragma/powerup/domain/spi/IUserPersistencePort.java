package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.UserModel;

import java.util.Optional;

public interface IUserPersistencePort {
    UserModel saveUser(UserModel userModel);
    boolean existsByEmail(String email);
    boolean existsByDocumentNumber(String documentNumber);
    Optional<UserModel> getUserById(Long id);
}
