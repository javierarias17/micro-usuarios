package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.AuthRequestDto;
import com.pragma.powerup.application.dto.response.AuthResponseDto;
import com.pragma.powerup.application.handler.IAuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "auth-rest-controller", description = "Endpoints for user authentication")
public class AuthRestController {

    private final IAuthHandler authHandler;

    @Operation(
            summary = "Login",
            description = "Authenticates a user with email and password. "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Success",
                                    value = "{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHByYWdtYS5jb20iLCJyb2xlIjoiQURNSU4iLCJyb2xlSWQiOjEsInVzZXJJZCI6MSwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwMDcyMDB9.signature\"}"
                            ))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Invalid credentials",
                                    value = "{\"message\":\"Invalid email or password\"}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid email",
                                            value = "{\"message\":\"Validation failed\",\"errors\":[{\"field\":\"email\",\"message\":\"Email must have a valid format\"}]}"
                                    ),
                                    @ExampleObject(
                                            name = "Blank password",
                                            value = "{\"message\":\"Validation failed\",\"errors\":[{\"field\":\"password\",\"message\":\"Password is required\"}]}"
                                    )
                            }))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authHandler.login(authRequestDto));
    }
}
