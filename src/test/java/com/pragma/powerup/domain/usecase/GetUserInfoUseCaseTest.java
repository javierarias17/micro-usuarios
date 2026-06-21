package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.NotFoundException;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
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
class GetUserInfoUseCaseTest {

    private static final long EMPLOYEE_ID = 1L;
    private static final long CUSTOMER_ID = 10L;
    private static final long RESTAURANT_ID = 5L;
    private static final long NON_EXISTENT_ID = 99L;
    private static final String PHONE = "+573197633852";

    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private GetUserInfoUseCase getUserInfoUseCase;

    // ─── getRestaurantIdByEmployeeId: Happy path

    @Test
    void When_EmployeeIsLinkedToRestaurant_Expect_RestaurantIdReturned() {
        // Arrange
        when(userPersistencePort.findRestaurantIdByEmployeeId(EMPLOYEE_ID))
                .thenReturn(Optional.of(RESTAURANT_ID));

        // Act
        Long result = getUserInfoUseCase.getRestaurantIdByEmployeeId(EMPLOYEE_ID);

        // Assert
        assertEquals(RESTAURANT_ID, result);
    }

    // ─── getRestaurantIdByEmployeeId: Exception path

    @Test
    void Expect_NotFoundException_When_EmployeeIsNotLinkedToAnyRestaurant() {
        // Arrange
        when(userPersistencePort.findRestaurantIdByEmployeeId(NON_EXISTENT_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> getUserInfoUseCase.getRestaurantIdByEmployeeId(NON_EXISTENT_ID));
    }

    // ─── getPhoneByCustomerId: Happy path

    @Test
    void When_CustomerExists_Expect_PhoneReturned() {
        // Arrange
        when(userPersistencePort.findPhoneByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(PHONE));

        // Act
        String result = getUserInfoUseCase.getPhoneByCustomerId(CUSTOMER_ID);

        // Assert
        assertEquals(PHONE, result);
    }

    // ─── getPhoneByCustomerId: Exception path

    @Test
    void Expect_NotFoundException_When_CustomerDoesNotExist() {
        // Arrange
        when(userPersistencePort.findPhoneByCustomerId(NON_EXISTENT_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> getUserInfoUseCase.getPhoneByCustomerId(NON_EXISTENT_ID));
    }
}
