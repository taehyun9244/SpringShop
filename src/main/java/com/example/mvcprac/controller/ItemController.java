package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.service.ItemService;
import com.example.mvcprac.service.file.FileStore;
import com.example.mvcprac.validation.CurrentAccount;
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
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemServiceImpl;
    private final FileStore fileStore;

    /**
     * findById item
     */
    @GetMapping("/{id}")
    public String findItem(@PathVariable Long id, Model model) {

        ItemDetailDto itemDetailDto = itemServiceImpl.findById(id);
        model.addAttribute("itemDetailDto", itemDetailDto);

        return "item/itemDetail";
    }

    @GetMapping("/add")
    public String addItem(@ModelAttribute(name = "form") ItemForm form) {
        return "item/itemForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute(name = "form") ItemForm form, @CurrentAccount Account account,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemForm";
        }

        Long itemId = itemServiceImpl.createItem(form, account);
        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/items/{itemId}";
    }

    //TODO 아이템 수정
    @GetMapping("/edit/{id}")
    public String editItemView(@PathVariable Long id, Model model) {
        ItemDetailDto findItemById = itemServiceImpl.findById(id);
        model.addAttribute("form", findItemById);
        return "item/itemEdit";
    }

    @PostMapping("/edit")
    public String editItem(@Validated @ModelAttribute(name = "form") ItemForm form, @CurrentAccount Account account,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemForm";
        }

        Long itemId = itemServiceImpl.editItem(form, account);
        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/items/{itemId}";
    }


    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:"+ fileStore.getFullPath(fileName));
    }

    //TODO 아이템 삭제
    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemServiceImpl.deleteItem(id);
        return "home";
    }

}
