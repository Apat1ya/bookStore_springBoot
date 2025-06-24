package com.bookstore.validator;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements
        ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto requestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (requestDto.getPassword() == null || requestDto.getRepeatPassword() == null) {
            return false;
        }
        return requestDto.getPassword().equals(requestDto.getRepeatPassword());
    }
}
