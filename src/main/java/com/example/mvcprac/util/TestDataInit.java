//package com.example.mvcprac.util;
//
//
//import com.example.mvcprac.dto.account.SignupDto;
//import com.example.mvcprac.repository.AccountRepository;
//import com.example.mvcprac.service.AccountService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@RequiredArgsConstructor
//public class TestDataInit {
//
//    private final AccountRepository accountRepository;
//    private final AccountService accountService;
//
//    /**
//     * 테스트용 데이터 추가
//     */
//    @PostConstruct
//    public void init() {
//        SignupDto SignupDto = new SignupDto(
//                        "남태현",
//                        "시모키타자와",
//                        "12341234!a",
//                        "namtaehyun9244@gmail.com",
//                        "서울특별시 서초구 서초동",
//                        "01012345678",
//                        "19920404");
//        accountService.createUser(SignupDto);
//    }
//}
