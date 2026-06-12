package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.EmployeeRequestDto;
import com.pragma.powerup.application.dto.request.OwnerRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface IUserRestControllerDocs {

        @Operation(summary = "Create owner", description = "Creates a new owner user. Requires ADMIN role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Owner created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid fields or owner is not of legal age", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                                        @ExampleObject(name = "Validation failed", value = "{\"message\":\"Validation failed\",\"errors\":[{\"field\":\"birthDate\",\"message\":\"Birth date is required\"},{\"field\":\"email\",\"message\":\"Email must have a valid format\"}]}"),
                                        @ExampleObject(name = "Not adult", value = "{\"message\":\"Business validation failed\",\"errors\":[{\"field\":\"birthDate\",\"message\":\"Owner must be of legal age\"}]}")
                        })),
                        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"No authentication token provided.\"}"))),
                        @ApiResponse(responseCode = "403", description = "Authenticated user does not have ADMIN role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Access Denied\"}"))),
                        @ApiResponse(responseCode = "409", description = "Email or document number already exists in the system", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Business validation failed\",\"errors\":[{\"field\":\"email\",\"message\":\"Mail already exists in the system\"}]}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"An unexpected error occurred. Please contact the administrator.\"}")))
        })
        ResponseEntity<UserResponseDto> createOwner(OwnerRequestDto ownerRequestDto);

        @Operation(summary = "Create employee", description = "Creates a new employee user account. Requires OWNER role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid fields", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Business validation failed\",\"errors\":[{\"field\":\"email\",\"message\":\"Email must have a valid format\"}]}"))),
                        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"No authentication token provided.\"}"))),
                        @ApiResponse(responseCode = "403", description = "Authenticated user does not have OWNER role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Access Denied\"}"))),
                        @ApiResponse(responseCode = "409", description = "Email or document number already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Business validation failed\",\"errors\":[{\"field\":\"email\",\"message\":\"Mail already exists in the system\"}]}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"An unexpected error occurred. Please contact the administrator.\"}")))
        })
        ResponseEntity<UserResponseDto> createEmployee(EmployeeRequestDto employeeRequestDto);

        @Operation(summary = "Check if user is owner", description = "Returns true if the user with the given ID has the OWNER role. Requires ADMIN role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Validation result returned", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "true"))),
                        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"No authentication token provided.\"}"))),
                        @ApiResponse(responseCode = "403", description = "Authenticated user does not have ADMIN role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Access Denied\"}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"User not found in the system\"}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"An unexpected error occurred. Please contact the administrator.\"}")))
        })
        ResponseEntity<Boolean> isOwner(Long id);

        @Operation(summary = "Check if user is employee", description = "Returns true if the user with the given ID has the EMPLOYEE role. Requires OWNER role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Validation result returned", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "true"))),
                        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"No authentication token provided.\"}"))),
                        @ApiResponse(responseCode = "403", description = "Authenticated user does not have OWNER role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"Access Denied\"}"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"User not found in the system\"}"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = @ExampleObject(value = "{\"message\":\"An unexpected error occurred. Please contact the administrator.\"}")))
        })
        ResponseEntity<Boolean> isEmployee(Long id);
}
