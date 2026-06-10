package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "JWT token returned after successful authentication")
public class AuthResponseDto {

    @Schema(description = "Bearer JWT token.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHByYWdtYS5jb20iLCJyb2xlIjoiQURNSU4iLCJyb2xlSWQiOjEsInVzZXJJZCI6MX0.signature")
    private String token;
}
