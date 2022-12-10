package com.example.mvcprac.controller;

import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.dto.meeting.MeetingForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.repository.MeetingRepository;
import com.example.mvcprac.service.AccountService;
import com.example.mvcprac.service.MeetingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MeetingController {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MeetingService meetingService;
    @Autowired
    MeetingRepository meetingRepository;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm(
                "남태현",
                "12341234!a",
                "19920404",
                "테스트닉네임",
                "email@email.com",
                "서울",
                "01012345678");
        accountService.createUser(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Meeting 개설 form")
    void createMeetingForm() throws Exception {
        mockMvc.perform(get("/new-meeting"))
                .andExpect(status().isOk())
                .andExpect(view().name("meeting/meetingForm"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("meetingForm"));
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Meeting 개설 성공")
    void createMeeting_success() throws Exception {
        mockMvc.perform(post("/new-meeting")
                        .param("path", "test-path")
                        .param("title", "meeting title")
                        .param("shortDescription", "short description of a meeting")
                        .param("fullDescription", "full description of a meeting")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meeting/test-path"));

        Meeting meeting = meetingRepository.findByPath("test-path");
        assertNotNull(meeting);
        Account account = accountRepository.findByPhoneNumber("email@email.com");
        assertTrue(meeting.getManagers().contains(account));
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Meeting 개설 실패")
    void createMeeting_fail() throws Exception {
        mockMvc.perform(post("/new-meeting")
                .param("path", "wrong-path")
                .param("title", "meeting title")
                .param("shortDescription", "short description of a meeting")
                .param("fullDescription", "full description of a meeting")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("meeting/meetingForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("meetingForm"));

        Meeting meeting = meetingRepository.findByPath("test-path");
        assertNull(meeting);
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Meeting 조회 성공")
    void viewStudy_success() throws Exception {
        MeetingForm meetingForm = new MeetingForm(
                "test-path", "test meeting", "short description", "<p>full description</p>");

        Account simokitazawa = accountRepository.findByNickname("시모키타자와");
        meetingService.createNewMeeting(meetingForm, simokitazawa);
        mockMvc.perform(get("/meeting/test-path"))
                .andExpect(view().name("meeting/meetingView"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("meeting"));
    }

    @Test
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Meeting 조회 실패")
    void viewStudy_fail() throws Exception {
        MeetingForm meetingForm = new MeetingForm(
                "test-path", "test meeting", "short description", "<p>full description</p>");

        Account simokitazawa = accountRepository.findByNickname("시모키타자와");
        meetingService.createNewMeeting(meetingForm, simokitazawa);
        mockMvc.perform(get("/meeting/fail-path"))
                .andExpect(status().is4xxClientError());
    }
}
