package com.example.mvcprac.service;

import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.dto.profile.Notifications;
import com.example.mvcprac.dto.profile.Profile;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.validation.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public Account createUser(SignUpForm signUpForm)  {
        Account newAccount = saveNewUser(signUpForm);
        newAccount.generateEmailCheckToke();
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewUser(SignUpForm signUpForm) {
        String password = signUpForm.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        Account account = new Account(signUpForm, encodePassword, true, true, true);
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("스프링 shop 회원 가입인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());

        javaMailSender.send(mailMessage);
    }

    public void login(Account newAccount) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(newAccount),
                newAccount.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrPhoneNumber) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrPhoneNumber);
        if (account == null) {
            account = accountRepository.findByPhoneNumber(emailOrPhoneNumber);
//            account = accountRepository.findByNickname(emailOrPhoneNumber);
        }

        if (account == null) {
            throw new UsernameNotFoundException(emailOrPhoneNumber);
        }

        return new UserAccount(account);
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    public void updateProfile(Account account, Profile profile) {
//        account.setBio(profile.getBio());
//        account.setUrl(profile.getUrl());
//        account.setOccupation(profile.getOccupation());
//        account.setLocation(profile.getLocation());
//        account.setProfileImage(profile.getProfileImage());
        modelMapper.map(profile, account);
        accountRepository.save(account);
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, Notifications notifications) {
//        account.setShopCreatedByWeb(notifications.isShopCreatedByWeb());
//        account.setShopCreatedByEmail(notifications.isShopCreatedByEmail());
//        account.setShopUpdatedByWeb(notifications.isShopUpdatedByWeb());
//        account.setShopUpdatedByEmail(notifications.isShopUpdatedByEmail());
//        account.setShopEnrollmentResultByEmail(notifications.isShopEnrollmentResultByEmail());
//        account.setShopEnrollmentResultByWeb(notifications.isShopEnrollmentResultByWeb());
        modelMapper.map(notifications, account);
        accountRepository.save(account);
    }
}
