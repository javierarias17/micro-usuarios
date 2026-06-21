package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RoleEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
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
        UserEntity userEntity = userEntityMapper.toEntity(userModel);
        userEntity.setRole(roleRepository.findById(userModel.getRole().getId()).orElse(null));
        return toModel(userRepository.save(userEntity));
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
        return userRepository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toModel);
    }

    @Override
    public Optional<Long> findRestaurantIdByEmployeeId(Long employeeId) {
        return userRepository.findRestaurantIdById(employeeId);
    }

    @Override
    public Optional<String> findPhoneByCustomerId(Long customerId) {
        return userRepository.findPhoneById(customerId);
    }

    private UserModel toModel(UserEntity entity) {
        if (entity == null) return null;
        RoleEntity roleEntity = entity.getRole();
        RoleModel role = roleEntity != null
                ? new RoleModel(roleEntity.getId(), roleEntity.getName(), roleEntity.getDescription())
                : null;
        return UserModel.reconstruct(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getDocumentNumber(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getEmail(),
                entity.getPassword(),
                role,
                entity.getRestaurantId()
        );
    }
}
