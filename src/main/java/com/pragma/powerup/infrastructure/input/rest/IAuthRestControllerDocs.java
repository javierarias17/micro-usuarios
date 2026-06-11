package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.AuthRequestDto;
import com.pragma.powerup.application.dto.response.AuthResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "auth-rest-controller", description = "Endpoints for user authentication")
public interface IAuthRestControllerDocs {

    @Operation(summary = "Login", description = "Authenticates a user with email and password. Returns a JWT token on success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponseDto.class),
                            examples = @ExampleObject(name = "Success", value = "{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHByYWdtYS5jb20iLCJyb2xlIjoiQURNSU4iLCJyb2xlSWQiOjEsInVzZXJJZCI6MSwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwMDcyMDB9.signature\"}"))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                            @ExampleObject(name = "Invalid email", value = "{\"message\":\"Validation failed\",\"errors\":[{\"field\":\"email\",\"message\":\"Email must have a valid format\"}]}"),
                            @ExampleObject(name = "Blank password", value = "{\"message\":\"Validation failed\",\"errors\":[{\"field\":\"password\",\"message\":\"Password is required\"}]}")
                    })),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(name = "Invalid credentials", value = "{\"message\":\"Invalid email or password\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\"message\":\"An unexpected error occurred. Please contact the administrator.\"}")))
    })
    ResponseEntity<AuthResponseDto> login(AuthRequestDto authRequestDto);
}
