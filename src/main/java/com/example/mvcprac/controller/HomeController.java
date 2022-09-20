package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.service.ItemService;
import com.example.mvcprac.service.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;
    private final FileStore fileStore;

    @GetMapping("/")
    public String home(Model model) {

        List<ItemListDto> itemListDto = itemService.findAll();
        log.info("itemAll", itemListDto);
        model.addAttribute("itemListDto", itemListDto);
        return "home";
    }

    @ResponseBody
    @GetMapping("/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:"+ fileStore.getFullPath(fileName));
    }
}
