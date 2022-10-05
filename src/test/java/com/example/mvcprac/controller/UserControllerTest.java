package com.example.mvcprac.controller;

import com.example.mvcprac.model.User;
import com.example.mvcprac.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    @DisplayName("회원가입 화면 보이는지 테스트")
    void signupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"))
                .andExpect(model().attributeExists("signUpForm"));
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
                .andExpect(view().name("user/register"));
    }

    @Test
    @DisplayName("회원가입 처리 입력값 정상")
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("name", "남태현")
                        .param("nickname", "시모키타자와")
                        .param("password", "123456789!")
                        .param("birthday", "920404")
                        .param("address", "명달로99길99")
                        .param("email", "email@email.com")
                        .param("phoneNumber", "01012345678")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/login"));

        User user = userRepository.findByEmail("taehyun@email.com");
        assertNotNull(user);
        assertNotEquals(user.getPassword(), "123456789");
        assertNotNull(user.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }

}