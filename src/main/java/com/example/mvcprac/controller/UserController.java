package com.example.mvcprac.controller;

import com.example.mvcprac.dto.user.LoginDto;
import com.example.mvcprac.dto.user.SignUpForm;
import com.example.mvcprac.service.UserService;
import com.example.mvcprac.validation.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping(value = "/signup")
    public String createUser(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "login/register";
    }

    @PostMapping(value="/signup")
    public String createUser(@Validated @ModelAttribute SignUpForm dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        log.info("Signup = {}", dto);

        //검증실패시
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "login/register";
        }

        //검증 성공시
        userService.createUser(dto);
        redirectAttributes.addAttribute("status", true);

        return "login/login";
    }

    @GetMapping(value="/login")
    public String login(Model model){
        model.addAttribute("loginDto", new LoginDto());
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDto dto,
                        BindingResult bindingResult){
        //검증실패시
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "login/login";
        }

        //검증성공시
        return "redirect:/";
    }

}
