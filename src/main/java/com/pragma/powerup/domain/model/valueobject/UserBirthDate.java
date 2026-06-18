package com.pragma.powerup.domain.model.valueobject;

import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;

import java.time.LocalDate;
import java.util.Map;

public record UserBirthDate(LocalDate value) {

    public static void validate(LocalDate value, Long roleId, Map<String, String> errors) {
        if (DomainConstants.OWNER_ROLE_ID.equals(roleId) && value == null)
            errors.put(FieldConstants.BIRTH_DATE, ValidationMessageConstants.MSG_BIRTH_DATE_REQUIRED);
    }
}
