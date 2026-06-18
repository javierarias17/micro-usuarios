package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserEntityMapper {

    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "lastName.value", target = "lastName")
    @Mapping(source = "documentNumber.value", target = "documentNumber")
    @Mapping(source = "phone.value", target = "phone")
    @Mapping(source = "birthDate.value", target = "birthDate")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "password.value", target = "password")
    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(UserModel userModel);
}
