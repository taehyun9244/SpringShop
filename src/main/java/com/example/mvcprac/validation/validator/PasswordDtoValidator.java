package com.example.mvcprac.validation.validator;

import com.example.mvcprac.dto.profile.PasswordDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordDto passwordDto = (PasswordDto) target;
        if (!passwordDto.getNewPassword().equals(passwordDto.getNewPasswordConfirm())) {
            errors.rejectValue("newPassword", "wrong.value", "새로운 패스워드가 일치하지 않습니다");
        }
   }
}
