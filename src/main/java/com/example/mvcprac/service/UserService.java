package com.example.mvcprac.service;

import com.example.mvcprac.dto.user.SignUpForm;
import com.example.mvcprac.model.User;
import com.example.mvcprac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(SignUpForm signUpForm) {
        User newUser = saveNewUser(signUpForm);
        newUser.generateEmailCheckToke();
        sendSignUpConfirmEmail(newUser);
    }

    private User saveNewUser(SignUpForm signUpForm) {
        String password = signUpForm.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        User user = new User(signUpForm, encodePassword, true, true, true);
        User newUser = userRepository.save(user);
        return newUser;
    }

    private void sendSignUpConfirmEmail(User newUser) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newUser.getEmail());
        mailMessage.setSubject("스프링 shop 회원 가입인증");
        mailMessage.setText("/check-email-token?token=" + newUser.getEmailCheckToken() +
                "&email=" + newUser.getEmail());

        javaMailSender.send(mailMessage);
    }
}
