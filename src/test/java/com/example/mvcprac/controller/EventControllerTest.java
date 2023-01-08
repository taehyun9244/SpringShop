package com.example.mvcprac.controller;

import com.example.mvcprac.MockMvcTest;
import com.example.mvcprac.factory.AccountFactory;
import com.example.mvcprac.factory.MeetingFactory;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Event;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.repository.EnrollmentRepository;
import com.example.mvcprac.service.EventService;
import com.example.mvcprac.util.status.EventEunm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class EventControllerTest {

    @Autowired
    EventService eventService;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    AccountFactory accountFactory;
    @Autowired
    MeetingFactory meetingFactory;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        accountFactory.createAccount("email.com");
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }


    @Test
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    void newEnrollment_to_FCFS_event_accepted() throws Exception {
        Account sibuya = accountFactory.createSibuya("sibuya@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", sibuya);
        Event event = createEvent("test-event", EventEunm.FCFS, 2, meeting, sibuya);

        mockMvc.perform(post("/meeting/" + meeting.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meeting/" + meeting.getPath() + "/events/" + event.getId()));

        Account email = accountRepository.findByEmail("email@email.com");
        isAccepted(email, event);
    }

    @Test
    @DisplayName("선착순 모임에 참가 신청 - 대기중 (이미 인원이 꽉차서)")
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    void newEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account sibuya = accountFactory.createSibuya("sibuya@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", sibuya);
        Event event = createEvent("test-event", EventEunm.FCFS, 2, meeting, sibuya);

        Account sinjuku = accountFactory.createSinjuku("sinjuku@email.com");
        Account simokitazawa = accountFactory.createSimokitazawa("simokitazawa@email.com");
        eventService.newEnrollment(event, sinjuku);
        eventService.newEnrollment(event, simokitazawa);

        mockMvc.perform(post("/meeting/" + meeting.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meeting/" + meeting.getPath() + "/events/" + event.getId()));

        Account email = accountRepository.findByEmail("email@email.com");
        isNotAccepted(email, event);
    }

    @Test
    @DisplayName("참가신청 확정자가 선착순 모임에 참가 신청을 취소하는 경우, 바로 다음 대기자를 자동으로 신청 확인한다.")
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    void accepted_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account email = accountRepository.findByEmail("email@email.com");
        Account sibuya = accountFactory.createSibuya("sibuya@email.com");
        Account sinjuku = accountFactory.createSinjuku("sinjuku@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", sibuya);
        Event event = createEvent("test-event", EventEunm.FCFS, 2, meeting, sibuya);

        eventService.newEnrollment(event, email);
        eventService.newEnrollment(event, sibuya);
        eventService.newEnrollment(event, sinjuku);

        isAccepted(email, event);
        isAccepted(sibuya, event);
        isNotAccepted(sinjuku, event);

        mockMvc.perform(post("/meeting/" + meeting.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meeting/" + meeting.getPath() + "/events/" + event.getId()));

        isAccepted(sibuya, event);
        isAccepted(sinjuku, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, email));
    }


    @Test
    @DisplayName("참가신청 비확정자가 선착순 모임에 참가 신청을 취소하는 경우, 기존 확정자를 그대로 유지하고 새로운 확정자는 없다.")
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    void not_accepterd_account_cancelEnrollment_to_FCFS_event_not_accepted() throws Exception {
        Account email = accountRepository.findByEmail("email@email.com");
        Account sibuya = accountFactory.createSibuya("sibuya@email.com");
        Account sinjuku = accountFactory.createSinjuku("sinjuku@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", sibuya);
        Event event = createEvent("test-event", EventEunm.FCFS, 2, meeting, sinjuku);

        eventService.newEnrollment(event, sinjuku);
        eventService.newEnrollment(event, sibuya);
        eventService.newEnrollment(event, email);

        isAccepted(sinjuku, event);
        isAccepted(sibuya, event);
        isNotAccepted(email, event);

        mockMvc.perform(post("/meeting/" + meeting.getPath() + "/events/" + event.getId() + "/disenroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meeting/" + meeting.getPath() + "/events/" + event.getId()));

        isAccepted(sinjuku, event);
        isAccepted(sibuya, event);
        assertNull(enrollmentRepository.findByEventAndAccount(event, email));
    }

    @Test
    @DisplayName("관리자 확인 모임에 참가 신청 - 대기중")
    @WithUserDetails(value = "email@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    void newEnrollment_to_CONFIMATIVE_event_not_accepted() throws Exception {
        Account sibuya = accountFactory.createSibuya("sibuya@email.com");
        Meeting meeting = meetingFactory.createMeeting("test-study", sibuya);
        Event event = createEvent("test-event", EventEunm.CONFIRMATIVE, 2, meeting, sibuya);

        mockMvc.perform(post("/meeting/" + meeting.getPath() + "/events/" + event.getId() + "/enroll")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meeting/" + meeting.getPath() + "/events/" + event.getId()));

        Account email = accountRepository.findByEmail("email@email.com");
        isNotAccepted(email, event);
    }

    private void isAccepted(Account account, Event event) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
    }

    private void isNotAccepted(Account email, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event, email).isAccepted());
    }

    private Event createEvent(String eventTitle, EventEunm eventEunm, int limit, Meeting meeting, Account account) {
        Event event = new Event();
        event.setEventEunm(eventEunm);
        event.setLimitOfEnrollments(limit);
        event.setTitle(eventTitle);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setEndEnrollmentDateTime(LocalDateTime.now().plusDays(1));
        event.setStartDateTime(LocalDateTime.now().plusDays(1).plusHours(5));
        event.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(7));
        return eventService.createEvent(event, meeting, account);
    }
}