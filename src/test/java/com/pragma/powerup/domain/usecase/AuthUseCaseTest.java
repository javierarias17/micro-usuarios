package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.InvalidCredentialsException;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.ITokenServicePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.factory.UserModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    private static final String EMAIL = "armando-diaz@gmail.com";
    private static final String RAW_PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "encodedSecret123";
    private static final String JWT_TOKEN = "jwt.token.signed";

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @Mock
    private ITokenServicePort tokenServicePort;

    @InjectMocks
    private AuthUseCase authUseCase;

    private RoleModel ownerRole;
    private UserModel savedUser;

    @BeforeEach
    void setUp() {
        ownerRole = UserModelFactory.createOwnerRole();
        savedUser = UserModelFactory.createSavedUserWithPassword(ownerRole, ENCODED_PASSWORD);
    }

    // ─── Happy path

    @Test
    void When_CredentialsAreCorrect_Expect_TokenGeneratedWithCorrectArguments() {
        // Arrange
        when(userPersistencePort.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        when(passwordEncoderPort.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(tokenServicePort.generateToken(EMAIL, ownerRole.getName(), ownerRole.getId(), savedUser.getId()))
                .thenReturn(JWT_TOKEN);

        // Act
        String result = authUseCase.login(EMAIL, RAW_PASSWORD);

        // Assert
        assertEquals(JWT_TOKEN, result);
    }

    // ─── Exception path

    @Test
    void Expect_InvalidCredentialsException_When_EmailIsNotRegistered() {
        // Arrange
        when(userPersistencePort.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authUseCase.login(EMAIL, RAW_PASSWORD));
    }

    @Test
    void Expect_InvalidCredentialsException_When_PasswordDoesNotMatch() {
        // Arrange
        when(userPersistencePort.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        when(passwordEncoderPort.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class,
                () -> authUseCase.login(EMAIL, RAW_PASSWORD));
    }
}
