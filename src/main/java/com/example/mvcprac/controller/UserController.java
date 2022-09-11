package com.example.mvcprac.controller;

import com.example.mvcprac.dto.user.LoginDto;
import com.example.mvcprac.dto.user.SignupDto;
import com.example.mvcprac.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;


    @GetMapping(value = "/signup")
    public String createUser(Model model){
        model.addAttribute("signupDto", new SignupDto());
        return "login/register";
    }

    @PostMapping(value="/signup")
    public String createUser(@Validated @ModelAttribute SignupDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        log.info("Signup = {}", dto);

        //검증실패시
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "login/register";
        }

        //검증 성공시
        userServiceImpl.createUser(dto);
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

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response){
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null){
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//        }
//        return "redirect:/";
//    }

}
