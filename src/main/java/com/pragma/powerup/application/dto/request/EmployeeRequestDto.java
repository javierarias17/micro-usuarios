package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.domain.common.ValidationMessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "Request body to create an employee account and link them to a restaurant")
public class EmployeeRequestDto extends UserBaseRequestDto {

    @Schema(description = "ID of the restaurant to assign the employee", example = "1")
    @NotNull(message = ValidationMessageConstants.MSG_RESTAURANT_ID_REQUIRED)
    private Long restaurantId;
}
