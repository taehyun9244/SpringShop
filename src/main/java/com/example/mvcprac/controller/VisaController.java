package com.example.mvcprac.controller;

import com.example.mvcprac.dto.visa.VisaListDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.service.VisaService;
import com.example.mvcprac.validation.CurrentAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class VisaController {

    private final VisaService visaService;

    @GetMapping("/visa")
    public String visaHomeView(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }

        List<VisaListDto> visaList = visaService.findVisaList();
        model.addAttribute("visaList", visaList);
        return "visa/visa-home";
    }

}
