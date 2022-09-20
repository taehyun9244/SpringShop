package com.example.mvcprac.controller;

import com.example.mvcprac.dto.item.ItemAddDto;
import com.example.mvcprac.dto.item.ItemDetailDto;
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
     * 상품 상세페이지 불러오기
     */
    @GetMapping("/{id}")
    public String findItem(@PathVariable Long id, Model model) {

        ItemDetailDto itemDetailDto = itemServiceImpl.findById(id);
        log.info("itemDetail = {}", itemDetailDto);
        model.addAttribute("itemDetailDto", itemDetailDto);

        return "item/itemOne";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:"+ fileStore.getFullPath(fileName));
    }


    /**
     * 상품 등록페이지 가져오기
     */
    @GetMapping("/add")
    public String addItem(@ModelAttribute ItemAddDto itemAddDto) {
        return "item/itemAdd";
    }

    /**
     * 상품 등록하기
     */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("itemAddDto") ItemAddDto itemAddDto,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        log.info("itemAddDto = {}", itemAddDto);

        //validation 실패하면
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemAdd";
        }

        //validation 성공하면
        Long itemId = itemServiceImpl.createItem(itemAddDto);
        log.info("itemId = {}", itemId);

        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/user/items/{itemId}";
    }


    /**
     * 상품 수정하기
     */
    @PutMapping("/edit/{id}")
    public String editItem(@Validated @ModelAttribute("itemDetailDto") ItemAddDto editDto,
                           BindingResult bindingResult, @PathVariable Long id) throws IOException {
        //validation 실패하면
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/itemEdit";
        }

        //image file 존재여부 체크
        if (editDto.getImageFiles().get(0).isEmpty()) {
            return "item/itemEdit";
        }

        //validation 성공하면
        itemServiceImpl.editItem(editDto, id);
        return "redirect:/";
    }

    /**
     * 상품 삭제하기
     */
    @DeleteMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemServiceImpl.delete(id);
        return "redirect:/";
    }

}
