package com.example.mvcprac.controller;

import com.example.mvcprac.dto.user.LoginDto;
import com.example.mvcprac.dto.user.SignUpForm;
import com.example.mvcprac.model.User;
import com.example.mvcprac.repository.UserRepository;
import com.example.mvcprac.service.UserService;
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
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/signup")
    public String createUser(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "/user/signup";
    }

    @PostMapping("/signup")
    public String createUser(@Valid @ModelAttribute SignUpForm dto, BindingResult bindingResult){
        log.info("Signup = {}", dto);

        //검증실패시
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "/user/signup";
        }

        //검증 성공시
        userService.createUser(dto);
        return "user/login";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {

        User user = userRepository.findByEmail(email);
        String view = "user/checked-email";

        if (user == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if (!user.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        user.setEmailVerified(true);
        model.addAttribute("numberOfUser", userRepository.count());
        model.addAttribute("nickname", user.getNickname());
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

        //검증성공시
        return "redirect:home";
    }

}
