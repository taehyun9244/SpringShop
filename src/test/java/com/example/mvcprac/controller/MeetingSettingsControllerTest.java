package com.example.mvcprac.controller;

import com.example.mvcprac.MockMvcTest;
import com.example.mvcprac.factory.AccountFactory;
import com.example.mvcprac.factory.MeetingFactory;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.repository.MeetingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class MeetingSettingsControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected MeetingRepository meetingRepository;
    @Autowired
    AccountFactory accountFactory;
    @Autowired
    MeetingFactory meetingFactory;

    @BeforeEach
    void beforeEach() {
        accountFactory.createAccount("email@eamil.com");
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 폼 조회 - 실패 (권한 없는 유저)")
    void updateDescriptionForm_fail() throws Exception {

        Account sibuya = accountFactory.createAccount("sibuya@gmail.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", sibuya);

        mockMvc.perform(get("/meeting/" + meeting.getPath() + "/settings/description"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 폼 조회 - 성공")
    void updateDescriptionForm_success() throws Exception {
        Account email = accountRepository.findByEmail("email@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", email);

        mockMvc.perform(get("/meeting/" + meeting.getPath() + "/settings/description"))
                .andExpect(status().isOk())
                .andExpect(view().name("meeting/settings/description"))
                .andExpect(model().attributeExists("meetingDescriptionForm"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("meeting"));
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 - 실패")
    void updateDescription_fail() throws Exception {
        Account email = accountRepository.findByEmail("email@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", email);

        String settingsDescriptionUrl = "/meeting/" + meeting.getPath() + "/settings/description";
        mockMvc.perform(post(settingsDescriptionUrl)
                        .param("shortDescription", "")
                        .param("fullDescription", "full description")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("meetingDescriptionForm"))
                .andExpect(model().attributeExists("meeting"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 - 성공")
    void updateDescription_success() throws Exception {
        Account email = accountRepository.findByEmail("email@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", email);

        String settingsDescriptionUrl = "/meeting/" + meeting.getPath() + "/settings/description";
        mockMvc.perform(post(settingsDescriptionUrl)
                        .param("shortDescription", "short description")
                        .param("fullDescription", "full description")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(settingsDescriptionUrl))
                .andExpect(flash().attributeExists("message"));
    }

}