package com.example.mvcprac.controller;

import com.example.mvcprac.dto.school.SchoolDetailDto;
import com.example.mvcprac.dto.visa.VisaDetailDto;
import com.example.mvcprac.service.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SchoolController {

    private SchoolService schoolService;

    @GetMapping("/schools/{id}")
    public String findVisaIdView(@PathVariable Long id, Model model) {
        SchoolDetailDto schoolDetailDto = schoolService.findOneSchool(id);
        model.addAttribute("schoolDetailDto", schoolDetailDto);
        return "school/schoolDetail";
    }
}
