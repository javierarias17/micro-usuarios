package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.domain.common.ValidationMessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request body to create an owner")
public class OwnerRequestDto extends UserBaseRequestDto {

    @Schema(description = "User's birth date in yyyy-MM-dd format", example = "1990-05-15", type = "string", format = "date")
    @NotNull(message = ValidationMessageConstants.MSG_BIRTH_DATE_REQUIRED)
    private LocalDate birthDate;
}
