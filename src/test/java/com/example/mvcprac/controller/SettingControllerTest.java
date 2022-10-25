package com.example.mvcprac.controller;

import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.service.AccountService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm(
                "남태값",
                "12341234!a",
                "19920404",
                "시모키타자와",
                "email@email.com",
                "서울",
                "01012345678");
        accountService.createUser(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("프로필 수정하기 view")
    void updateProfileView() throws Exception{
        mockMvc.perform(get(SettingController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("프로필 수정하기 - 입력값 정상")
    void updateProfile() throws Exception{
        String bio = "수정하는 bio";
        mockMvc.perform(post(SettingController.SETTINGS_PROFILE_URL)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));

        Account taehyun = accountRepository.findByNickname("시모키타자와");
        assertThat(bio).isEqualTo(taehyun.getBio());
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("프로필 수정하기 - 입력값 에러")
    void updateProfile_error() throws Exception{
        String bio = "35자가 넘어가면 에러가 나기 때문에 오류가 테스트 코드는 실패해야만 한다. 자 그럼 이제 테스트코드를 실행해 보겠다";
        mockMvc.perform(post(SettingController.SETTINGS_PROFILE_URL)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account taehyun = accountRepository.findByNickname("시모키타자와");
        Assertions.assertNull(taehyun.getBio());
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("password 수정하기 view")
    void updatePassword_view() throws Exception {
        mockMvc.perform(get(SettingController.SETTINGS_PASSWORD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("password 수정하기 - 입력값 에러 - 불일치")
    void updatePassword_fail() throws Exception {
        mockMvc.perform(post(SettingController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678@a")
                        .param("newPasswordConfirm", "11111111@a")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("password 수정하기 - 입력값 정상")
    void updatePassword_success() throws Exception {
        mockMvc.perform(post(SettingController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678@a")
                        .param("newPasswordConfirm", "12345678@a")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingController.SETTINGS_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account byEmail = accountRepository.findByEmail("email@email.com");
        Assertions.assertTrue(passwordEncoder.matches("12345678@a", byEmail.getPassword()));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("nickname 수정하기 view")
    void updateNickname_view() throws Exception {
        mockMvc.perform(get(SettingController.SETTINGS_ACCOUNT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("nickname 수정하기 - 입력값 에러 - 불일치")
    void updateNickname_fail() throws Exception {
        mockMvc.perform(post(SettingController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", "이것은 엄청길게 쓴 것 입니다 30자가넘으면 안되는데 그냥 넘기고 에러를 읽으킨다")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("nicknameForm"))
                .andExpect(model().attributeExists("account"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("nickname 수정하기 - 입력값 정상")
    void updateNickname_success() throws Exception {
        mockMvc.perform(post(SettingController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", "시모키타자와44")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingController.SETTINGS_ACCOUNT_URL))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickname("시모키타자와44"));
    }

}