package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        List<ItemListDto> itemList = itemService.findAllItem();
        model.addAttribute("itemList", itemList);
        return "home";
    }
}
