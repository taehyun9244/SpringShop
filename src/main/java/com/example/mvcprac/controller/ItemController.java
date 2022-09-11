package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemDto;
import com.example.mvcprac.service.ItemService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user/items")
public class ItemController {
    private final ItemService itemServiceImpl;


    /**
     * 상품 상세페이지 불러오기
     */
    @GetMapping("/{id}")
    public String editItem(Model model, @PathVariable Long id) throws NotFoundException {
        itemServiceImpl.findById(id);
        model.addAttribute("itemDto", new ItemDto());
        return "item/itemOne";
    }


    /**
     * 상품 등록페이지 가져오기
     */
    @GetMapping("/add")
    public String addItem(Model model) {
        model.addAttribute("itemDto", new ItemDto());
        return "item/itemAdd";
    }

    /**
     * 상품 등록하기
     */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("itemDto") ItemDto addDto,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //validation 실패하면
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemAdd";
        }

        //validation 성공하면
        Long saveItem = itemServiceImpl.createItem(addDto);
        redirectAttributes.addAttribute("itemId", saveItem);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/user/item/add/{itemId}";
    }


    /**
     * 상품 수정하기
     */
    @PutMapping("/edit/{id}")
    public String editItem(@Validated @ModelAttribute("itemDto") ItemDto addDto,
                           BindingResult bindingResult, @PathVariable Long id) throws NotFoundException {
        //validation 실패하면
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemEdit";
        }

        //validation 성공하면
        itemServiceImpl.editItem(addDto, id);
        return "redirect:/";
    }

    /**
     * 상품 삭제하기
     */
    @DeleteMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) throws NotFoundException {
        itemServiceImpl.delete(id);
        return "redirect:/";
    }

}
