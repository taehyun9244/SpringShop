package com.example.mvcprac.validation;

import com.example.mvcprac.dto.item.ItemForm;
import com.example.mvcprac.dto.user.SignUpForm;
import com.example.mvcprac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ItemForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) errors;

        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용중이 이메일입니다");
        }

        if (userRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpForm.getNickname()}, "이미 사용중이 닉네임입니다");
        }

        if (userRepository.existsByPhoneNumber(signUpForm.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "invalid.phoneNumber", new Object[]{signUpForm.getPhoneNumber()}, "이미 사용중이 전화번호입니다");
        }

    }
}
