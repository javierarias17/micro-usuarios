package com.pragma.powerup.domain.model.valueobject;

import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;

import java.util.Map;

public record RestaurantId(Long value) {

    public static void validate(Long value, Long roleId, Map<String, String> errors) {
        if (DomainConstants.EMPLOYEE_ROLE_ID.equals(roleId) && value == null)
            errors.put(FieldConstants.RESTAURANT_ID, ValidationMessageConstants.MSG_RESTAURANT_ID_REQUIRED);
    }
}
