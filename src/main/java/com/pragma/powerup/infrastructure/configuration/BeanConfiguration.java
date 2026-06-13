package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.api.ICreateCustomerServicePort;
import com.pragma.powerup.domain.api.ICreateEmployeeServicePort;
import com.pragma.powerup.domain.api.ICreateOwnerServicePort;
import com.pragma.powerup.domain.api.IValidateUserRoleServicePort;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IRolePersistencePort;
import com.pragma.powerup.domain.spi.ITokenServicePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.usecase.AuthUseCase;
import com.pragma.powerup.domain.usecase.CreateCustomerUseCase;
import com.pragma.powerup.domain.usecase.CreateEmployeeUseCase;
import com.pragma.powerup.domain.usecase.CreateOwnerUseCase;
import com.pragma.powerup.infrastructure.out.jpa.adapter.RoleJpaAdapter;

import com.pragma.powerup.domain.usecase.ValidateUserRoleUseCase;
import com.pragma.powerup.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRoleEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRoleRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    private final IUserEntityMapper userEntityMapper;
    private final IRoleEntityMapper roleEntityMapper;

    private final IPasswordEncoderPort passwordEncoderPort;
    private final ITokenServicePort tokenServicePort;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, roleRepository, userEntityMapper);
    }

    @Bean
    public IRolePersistencePort rolePersistencePort(){
        return new RoleJpaAdapter(roleRepository, roleEntityMapper);
    }

    @Bean
    public ICreateOwnerServicePort createOwnerServicePort() {
        return new CreateOwnerUseCase(userPersistencePort(), passwordEncoderPort);
    }

    @Bean
    public ICreateCustomerServicePort createCustomerServicePort() {
        return new CreateCustomerUseCase(userPersistencePort(), passwordEncoderPort);
    }

    @Bean
    public ICreateEmployeeServicePort createEmployeeServicePort() {
        return new CreateEmployeeUseCase(userPersistencePort(), passwordEncoderPort);
    }

    @Bean
    public IValidateUserRoleServicePort validateUserRoleServicePort() {
        return new ValidateUserRoleUseCase(userPersistencePort());
    }

    @Bean
    public IAuthServicePort authServicePort() {
        return new AuthUseCase(userPersistencePort(), passwordEncoderPort, tokenServicePort);
    }
}
