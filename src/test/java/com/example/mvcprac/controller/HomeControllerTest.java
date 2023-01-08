package com.example.mvcprac.controller;

import com.example.mvcprac.MockMvcTest;
import com.example.mvcprac.dto.account.SignUpDto;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        SignUpDto signUpDto = new SignUpDto(
                        "남태현",
                        "12341234!a",
                        "19920404",
                        "시모키타자와",
                        "email@email.com",
                        "서울",
                        "01012345678");
        accountService.createUser(signUpDto);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일 로그인 성공 - email")
    void login_with_email() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "email@email.com")
                        .param("password", "12341234!a")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("시모키타자와"));

    }

    @Test
    @DisplayName("이메일 로그인 성공 - phoneNumber")
    void login_with_phoneNumber() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "01012345678")
                        .param("password", "12341234!a")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("시모키타자와"));

    }

    @DisplayName("로그인 실패")
    @Test
    void login_fail() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "fail")
                        .param("password", "9999999999999")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {

        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}