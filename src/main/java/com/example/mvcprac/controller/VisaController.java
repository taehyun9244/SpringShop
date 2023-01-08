package com.example.mvcprac.controller;

import com.example.mvcprac.dto.visa.VisaCreateDto;
import com.example.mvcprac.dto.visa.VisaDetailDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.service.VisaService;
import com.example.mvcprac.validation.annotation.CurrentAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/visas")
public class VisaController {

    private final VisaService visaService;

    @GetMapping("/{id}")
    public String findVisaIdView(@PathVariable Long id, Model model) {
        VisaDetailDto visaDetailDto = visaService.findOneVisa(id);
        model.addAttribute("visaDetailDto", visaDetailDto);
        return "visa/visaDetail";
    }

    @GetMapping("/post")
    public String createVisaView(@ModelAttribute(name = "visaForm") VisaCreateDto visaCreateDto, @CurrentAccount Account account,
                                 Model model) {
        model.addAttribute("account", account);
        return "visa/visaForm";
    }

    @PostMapping("/post")
    public String createVisa(@Validated @ModelAttribute(name = "visaForm") VisaCreateDto visaCreateDto, @CurrentAccount Account account,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "visa/visaForm";
        }
        Long visaId = visaService.createVisa(visaCreateDto, account);
        log.info("itemId = {}", visaId);

        redirectAttributes.addAttribute("visaId", visaId);
        return "redirect:/visas/{visaId}";
    }

    @PostMapping("/delete/{id}")
    public String deleteVisa(@CurrentAccount Account account, @PathVariable Long id) {
        visaService.deleteVisa(account, id);
        return "visa/visa-home";
    }

}
