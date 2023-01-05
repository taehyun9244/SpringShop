package com.example.mvcprac.model;

import com.example.mvcprac.MockMvcTest;
import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.service.AccountService;
import com.example.mvcprac.validation.validator.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MockMvcTest
class MeetingTest {

    Meeting meeting;
    UserAccount userAccount;

    Account createAccount;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void beforeEach() {
        meeting = new Meeting();
        SignUpForm signUpForm = new SignUpForm(
                "남태값",
                "12341234!a",
                "19920404",
                "시모키타자와",
                "email@email.com",
                "서울",
                "01012345678");
        createAccount = accountService.createUser(signUpForm);
        userAccount = new UserAccount(createAccount);

    }

    @DisplayName("교류회를 공개했고 인원 모집 중이고, 이미 멤버나 교류회 관리자가 아니라면 스터디 가입 가능")
    @Test
    void isJoinable() {
        meeting.setPublished(true);
        meeting.setRecruiting(true);

        assertTrue(meeting.isJoinable(userAccount));
    }

    @DisplayName("교류회를 공개했고 인원 모집 중이더라도, 교류회 관리자는 스터디 가입이 불필요하다.")
    @Test
    void isJoinable_false_for_manager() {
        meeting.setPublished(true);
        meeting.setRecruiting(true);
        meeting.addManager(createAccount);

        assertFalse(meeting.isJoinable(userAccount));
    }

    @DisplayName("교류회를 공개했고 인원 모집 중이더라도, 교류회 멤버는 스터디 재가입이 불필요하다.")
    @Test
    void isJoinable_false_for_member() {
        meeting.setPublished(true);
        meeting.setRecruiting(true);
        meeting.addMember(createAccount);

        assertFalse(meeting.isJoinable(userAccount));
    }

    @DisplayName("교류회가 비공개거나 인원 모집 중이 아니면 교류회 가입이 불가능하다.")
    @Test
    void isJoinable_false_for_non_recruiting_study() {
        meeting.setPublished(true);
        meeting.setRecruiting(false);

        assertFalse(meeting.isJoinable(userAccount));

        meeting.setPublished(false);
        meeting.setRecruiting(true);

        assertFalse(meeting.isJoinable(userAccount));
    }

    @DisplayName("교류회 관리자인지 확인")
    @Test
    void isManager() {
        meeting.addManager(createAccount);
        assertTrue(meeting.isManager(userAccount));
    }

    @DisplayName("교류회 멤버인지 확인")
    @Test
    void isMember() {
        meeting.addMember(createAccount);
        assertTrue(meeting.isMember(userAccount));
    }
}