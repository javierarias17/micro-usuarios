package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.CustomerRequestDto;
import com.pragma.powerup.application.dto.request.EmployeeRequestDto;
import com.pragma.powerup.application.dto.request.OwnerRequestDto;
import com.pragma.powerup.domain.model.UserCreateCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    UserCreateCommand toUserFromOwner(OwnerRequestDto ownerRequestDto);
    UserCreateCommand toUserFromEmployee(EmployeeRequestDto employeeRequestDto);
    UserCreateCommand toUserFromCustomer(CustomerRequestDto customerRequestDto);
}
