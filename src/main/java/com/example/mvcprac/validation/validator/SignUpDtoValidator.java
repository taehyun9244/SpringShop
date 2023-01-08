package com.example.mvcprac.validation.validator;

import com.example.mvcprac.dto.account.SignUpDto;
import com.example.mvcprac.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpDtoValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpDto signUpDto = (SignUpDto) target;

        if (accountRepository.existsByEmail(signUpDto.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpDto.getEmail()}, "이미 사용중인 이메일입니다");
        }

        if (accountRepository.existsByNickname(signUpDto.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpDto.getNickname()}, "이미 사용중인 닉네임입니다");
        }

        if (accountRepository.existsByPhoneNumber(signUpDto.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "invalid.phoneNumber", new Object[]{signUpDto.getPhoneNumber()}, "이미 사용중인 전화번호입니다");
        }

    }
}
