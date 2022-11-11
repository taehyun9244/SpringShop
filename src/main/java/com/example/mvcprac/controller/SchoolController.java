package com.example.mvcprac.controller;

import com.example.mvcprac.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

}
