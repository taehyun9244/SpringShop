package com.example.mvcprac.controller;

import com.example.mvcprac.dto.visa.VisaDetailDto;
import com.example.mvcprac.dto.visa.VisaForm;
import com.example.mvcprac.dto.visa.VisaListDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.service.VisaService;
import com.example.mvcprac.validation.CurrentAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/visa/{id}")
    public String findVisaIdView(@PathVariable Long id, Model model) {
        VisaDetailDto visaDetailDto = visaService.findOneVisa(id);
        model.addAttribute("visaDetailDto", visaDetailDto);
        return "visa/visaForm";
    }

    @PostMapping("/visa/add")
    public String writeVisa(@Validated @ModelAttribute(name = "visaForm") VisaForm visaForm, @CurrentAccount Account account,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "visa/visaForm";
        }
        Long visaId = visaService.createVisa(visaForm, account);
        log.info("itemId = {}", visaId);

        redirectAttributes.addAttribute("visaId", visaId);
        return "redirect:/visa/{visaId}";
    }

    @DeleteMapping("/visa/{id}")
    public String deleteVisa(@CurrentAccount Account account, @PathVariable Long id) {
        visaService.deleteVisa(account, id);
        return "visa/visa-home";
    }

}
