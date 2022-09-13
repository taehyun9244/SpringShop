package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model, Pageable pageable) throws Exception{
        Page<ItemListDto> findAllItem = itemService.findAll(pageable);
        log.info("itemAll", findAllItem);
        model.addAttribute("itemListDto", findAllItem);
        return "home";
    }
}
