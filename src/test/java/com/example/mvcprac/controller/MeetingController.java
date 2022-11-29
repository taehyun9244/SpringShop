package com.example.mvcprac.controller;

import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.repository.MeetingRepository;
import com.example.mvcprac.service.AccountService;
import com.example.mvcprac.service.MeetingService;
import lombok.RequiredArgsConstructor;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
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

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("Meeting 개설 form")
    void createMeetingForm() throws Exception {
        mockMvc.perform(get("/new-meeting"))
                .andExpect(status().isOk())
                .andExpect(view().name("meeting/meetingForm"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("meetingForm"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("Meeting 개설 성공")
    @Test
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
        Account account = accountRepository.findByPhoneNumber("01012345678");
        assertTrue(meeting.getManagers().contains(account));
    }

}
