package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    boolean existsByDocumentNumber(String documentNumber);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u.restaurantId FROM UserEntity u WHERE u.id = :id")
    Optional<Long> findRestaurantIdById(@Param("id") Long id);

    @Query("SELECT u.phone FROM UserEntity u WHERE u.id = :id")
    Optional<String> findPhoneById(@Param("id") Long id);
}
