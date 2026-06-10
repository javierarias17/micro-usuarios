package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.exception.TechnicalException;
import com.pragma.powerup.domain.exception.TechnicalExceptionResponse;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRoleRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public UserModel saveUser(UserModel userModel) {
        var userEntity = userEntityMapper.toEntity(userModel);
        if (userModel.getRole() != null) {
            userEntity.setRole(Optional.ofNullable(userModel.getRole().getId())
                    .flatMap(roleRepository::findById)
                    .orElseThrow(() -> new TechnicalException(TechnicalExceptionResponse.ROLE_NOT_FOUND.getMessage())));
        }
        return userEntityMapper.toUserModel(userRepository.save(userEntity));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return userRepository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toUserModel);
    }
}
