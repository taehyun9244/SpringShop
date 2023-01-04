package com.example.mvcprac.factory;

import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(String email) {
        SignUpForm signUpForm = new SignUpForm(
                "남태현",
                "12341234!a",
                "19920404",
                "시모키타자와",
                "email@email.com",
                "서울",
                "01099999999");
        Account account = accountService.createUser(signUpForm);
        accountRepository.save(account);
        return account;
    }
    public Account createSibuya(String email) {
        SignUpForm signUpForm = new SignUpForm(
                "시부야",
                "12341234!a",
                "20020202",
                "시부야",
                email,
                "서울",
                "01011111111");
        Account account = accountService.createUser(signUpForm);
        accountRepository.save(account);
        return account;
    }
    public Account createSinjuku(String email) {
        SignUpForm signUpForm = new SignUpForm(
                "신주쿠",
                "12341234!a",
                "20220202",
                "신주쿠",
                email,
                "서울",
                "01022222222");
        Account account = accountService.createUser(signUpForm);
        accountRepository.save(account);
        return account;
    }

    public Account createSimokitazawa(String email) {
        SignUpForm signUpForm = new SignUpForm(
                "시모키타자와",
                "12341234!a",
                "20101010",
                "시모키타자와",
                email,
                "서울",
                "01033333333");
        Account account = accountService.createUser(signUpForm);
        accountRepository.save(account);
        return account;
    }
}
