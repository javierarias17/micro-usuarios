package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserRestController {

        private final IUserHandler userHandler;

        @Operation(summary = "Create user", description = "Creates a new user. Requires ADMIN role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid fields or user is not of legal age", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                @ExampleObject(name = "Validation failed", value = "{\"message\":\"Validation failed\",\"errors\":[{\"field\":\"birthDate\",\"message\":\"Birth date is required\"},{\"field\":\"email\",\"message\":\"Email must have a valid format\"}]}"),
                                @ExampleObject(name = "Not adult", value = "{\"message\":\"Business validation failed\",\"errors\":[{\"field\":\"birthDate\",\"message\":\"User must be of legal age\"}]}")
                        })),
                        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"No authentication token provided.\"}"))),
                        @ApiResponse(responseCode = "403", description = "Authenticated user does not have ADMIN role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Access Denied\"}"))),
                        @ApiResponse(responseCode = "409", description = "Email or document number already exists in the system", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Business validation failed\",\"errors\":[{\"field\":\"email\",\"message\":\"Mail already exists in the system\"}]}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"An unexpected error occurred. Please contact the administrator.\"}")))
        })
        @PostMapping("/")
        public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userHandler.createUser(userRequestDto));
        }

        @Operation(summary = "Check if user is owner", description = "Returns true if the user with the given ID has the OWNER role. Requires ADMIN role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Validation result returned", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "true"))),
                        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"No authentication token provided.\"}"))),
                        @ApiResponse(responseCode = "403", description = "Authenticated user does not have ADMIN role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Access Denied\"}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"User not found in the system\"}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"An unexpected error occurred. Please contact the administrator.\"}")))
        })
        @GetMapping("/{id}/is-owner")
        public ResponseEntity<Boolean> isOwner(@PathVariable Long id) {
                return ResponseEntity.ok(userHandler.isOwner(id));
        }
}
