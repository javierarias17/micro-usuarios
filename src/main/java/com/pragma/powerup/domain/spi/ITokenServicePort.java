package com.pragma.powerup.domain.spi;

public interface ITokenServicePort {
    String generateToken(String email, String role, Long roleId, Long userId);

    String getEmailFromToken(String token);

    String getRoleFromToken(String token);

    Long getRoleIdFromToken(String token);
}
