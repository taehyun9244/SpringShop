package com.example.mvcprac.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    /**
     * store view
     */
    @GetMapping("/store")
    public String storeView() {
        return "store/store-home";
    }

    @GetMapping("/store/add")
    public String storeFormView() {
        return "store/storeForm";
    }
}
