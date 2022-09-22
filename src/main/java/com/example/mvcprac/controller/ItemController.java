package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemForm;
import com.example.mvcprac.service.ItemService;
import com.example.mvcprac.service.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user/items")
public class ItemController {
    private final ItemService itemServiceImpl;
    private final FileStore fileStore;


    /**
     * findById item
     */
    @GetMapping("/{id}")
    public String findItem(@PathVariable Long id, Model model) {

        ItemDetailDto itemDetailDto  = itemServiceImpl.findById(id);
        log.info("itemDetail = {}", itemDetailDto);
        model.addAttribute("itemDetailDto", itemDetailDto);

        return "item/itemDetail";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:"+ fileStore.getFullPath(fileName));
    }


    /**
     * create Item Form
     */
    @GetMapping("/add")
    public String addItem(@ModelAttribute(name = "form") ItemForm form) {
        return "item/itemForm";
    }


    /**
     * create Item
     */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute(name = "form") ItemForm form,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        log.info("itemForm = {}", form);

        //validation 실패하면
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemForm";
        }

        //validation 성공하면
        Long itemId = itemServiceImpl.createItem(form);
        log.info("itemId = {}", itemId);

        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/user/items/{itemId}";
    }


}
