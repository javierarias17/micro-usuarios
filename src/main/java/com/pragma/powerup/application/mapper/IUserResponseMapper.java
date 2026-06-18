package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {

    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "lastName.value", target = "lastName")
    @Mapping(source = "documentNumber.value", target = "documentNumber")
    @Mapping(source = "phone.value", target = "phone")
    @Mapping(source = "birthDate.value", target = "birthDate")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "role.id", target = "idRole")
    UserResponseDto toResponse(UserModel userModel);
}
