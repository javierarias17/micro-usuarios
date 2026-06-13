package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CustomerRequestDto;
import com.pragma.powerup.application.dto.request.EmployeeRequestDto;
import com.pragma.powerup.application.dto.request.OwnerRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class UserRestController implements IUserRestControllerDocs {

    private final IUserHandler userHandler;

    @Override
    @PostMapping("/owner")
    public ResponseEntity<UserResponseDto> createOwner(@Valid @RequestBody OwnerRequestDto ownerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userHandler.createOwner(ownerRequestDto));
    }

    @Override
    @PostMapping("/employee")
    public ResponseEntity<UserResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userHandler.createEmployee(employeeRequestDto));
    }

    @Override
    @PostMapping("/customer")
    public ResponseEntity<UserResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userHandler.createCustomer(customerRequestDto));
    }

    @Override
    @GetMapping("/{id}/is-owner")
    public ResponseEntity<Boolean> isOwner(@PathVariable Long id) {
        return ResponseEntity.ok(userHandler.isOwner(id));
    }

    @Override
    @GetMapping("/{id}/is-employee")
    public ResponseEntity<Boolean> isEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(userHandler.isEmployee(id));
    }
}
