package com.example.mvcprac.controller;

import com.example.mvcprac.model.Account;
import com.example.mvcprac.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    @DisplayName("인증메일 확인 - 입력값 오류")
    void checkEmailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
                        .param("token", "qwerasdfzxcv")
                        .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("user/checked-email"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("인증메일 확인 - 입력값 정상")
    void checkEmailToken() throws Exception {

        Account account = new Account(1L, "남태현", "시모키타자와", "12345678!a",
                "email@email.com", "서울서초", "01012345678", "1992.04.04");
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                        .param("token", newAccount.getEmailCheckToken())
                        .param("email", newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("user/checked-email"))
                .andExpect(authenticated().withUsername("시모키타자와"));
    }

    @Test
    @DisplayName("회원가입 화면 보이는지 테스트")
    void signupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());;
    }

    @Test
    @DisplayName("회원가입 처리 입력값 오류")
    void signUpSubmit_with_wrong_input() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("name", "남태현")
                        .param("nickname", "시모키타자와")
                        .param("password", "123456789!")
                        .param("birthday", "920404")
                        .param("address", "명달로99길99")
                        .param("email", "email@..")
                        .param("phoneNumber", "01012345678")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("회원가입 처리 입력값 정상")
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("name", "남태현")
                        .param("nickname", "시모키타자와")
                        .param("password", "a123456789!")
                        .param("birthday", "1992.04.04")
                        .param("address", "명달로99길99")
                        .param("email", "email@email.com")
                        .param("phoneNumber", "01012345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("시모키타자와"));

        Account account = accountRepository.findByEmail("taehyun@email.com");
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "123456789");
        assertNotNull(account.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }

}