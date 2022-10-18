//package com.example.mvcprac.util;
//
//
//import com.example.mvcprac.dto.account.SignUpForm;
//import com.example.mvcprac.model.Account;
//import com.example.mvcprac.repository.AccountRepository;
//import com.example.mvcprac.repository.ItemRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@RequiredArgsConstructor
//public class TestDataInit {
//
//    private final ItemRepository itemRepository;
//    private final AccountRepository accountRepository;
//
//    /**
//     * 테스트용 데이터 추가
//     */
//    @PostConstruct
//    public void init() {
//        SignUpForm signUpForm = new SignUpForm("남태현", "시모키타자와", "12341234!a", "namtaehyun9244@gmail.com", "서울특별시 서초구 서초동", "01012345678", "1992.04.04", );
//        Account account = new Account(signUpForm);
//        accountRepository.save(account);
//
//    }
//}
