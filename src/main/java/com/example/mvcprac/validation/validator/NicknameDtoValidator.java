package com.example.mvcprac.validation.validator;

import com.example.mvcprac.dto.account.NicknameDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class NicknameDtoValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameDto nicknameDto = (NicknameDto) target;
        Account byNickname = accountRepository.findByNickname(nicknameDto.getNickname());
        if (byNickname != null) {
            errors.rejectValue("nickname", "wrong.value", "입력하신 닉네임을 사용할 수 없습니다.");
        }
    }
}
