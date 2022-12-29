package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.service.ItemService;
import com.example.mvcprac.service.file.FileStore;
import com.example.mvcprac.validation.customize.CurrentAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ItemService itemService;
    private final FileStore fileStore;
    private final ModelMapper modelMapper;


    @GetMapping("/{id}")
    public String itemView(@PathVariable Long id, Model model) {

        ItemDetailDto itemDetailDto = itemService.findById(id);
        model.addAttribute("itemDetailDto", itemDetailDto);

        return "/item/itemView";
    }

    @GetMapping("/add")
    public String itemFormView(@ModelAttribute(name = "form") ItemForm form) {
        return "item/itemForm";
    }

    @PostMapping("/add")
    public String itemFormSubmit(@Validated @ModelAttribute(name = "form") ItemForm form, @CurrentAccount Account account,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemForm";
        }

        Long itemId = itemService.createItem(form, account);
        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{id}/edit")
    public String editItemView(@PathVariable Long id, Model model) {
        Item item = itemService.getItemId(id);
        itemService.deleteImages(item);
        model.addAttribute(modelMapper.map(item, ItemForm.class));
        return "item/itemEdit";
    }

    @PostMapping("/{id}/edit")
    public String editItemSubmit(@Validated @ModelAttribute ItemForm itemForm, @PathVariable Long id, @CurrentAccount Account account,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws IOException {

        Item item = itemService.getItemId(id);
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            model.addAttribute(item);
            model.addAttribute(account);
            return "item/itemEdit";
        }

        Long editItemId = itemService.editItem(item, itemForm, account);
        log.info("editItemId = {}", editItemId);
        redirectAttributes.addAttribute("itemId", editItemId);
        //TODO 이미지 존재하지 않을때 기본이미지 넣어주기
        return "redirect:/items/{itemId}";
    }

    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return "home";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource imagesUpload(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }
}
