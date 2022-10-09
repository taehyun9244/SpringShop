package com.example.mvcprac.controller;

import com.example.mvcprac.dto.account.LoginDto;
import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @GetMapping("/signup")
    public String createUser(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String createUser(@Valid @ModelAttribute SignUpForm dto, BindingResult bindingResult){
        //검증실패시
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "user/signup";
        }

        //검증 성공시
        Account account = accountService.createUser(dto);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "user/checked-email";

        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if (!account.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        account.completeSignUp();
        accountService.login(account);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginDto", new LoginDto());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDto dto,
                        BindingResult bindingResult){
        //검증실패시
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "user/login";
        }

        //TODO 로그인
//        User loginUser = userService.login(dto);

        //검증성공시
        return "redirect:/";
    }

}
