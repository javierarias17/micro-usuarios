package com.pragma.powerup.domain.api;

public interface IValidateUserRoleServicePort {
    boolean isOwner(Long userId);
}
