package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.dto.item.ItemSearchDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.service.ItemService;
import com.example.mvcprac.service.file.FileStore;
import com.example.mvcprac.validation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;
    private final FileStore fileStore;

    @GetMapping("/")
    public String home(@CurrentUser Account account, ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

        if (account != null) {
            model.addAttribute(account);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<ItemListDto> itemList = itemService.findAllItem(itemSearchDto, pageable);

        model.addAttribute("items", itemList);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "home";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:"+ fileStore.getFullPath(fileName));
    }
}
