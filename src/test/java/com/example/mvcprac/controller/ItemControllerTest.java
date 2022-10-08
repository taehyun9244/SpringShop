package com.example.mvcprac.controller;

import com.example.mvcprac.repository.ImageRepository;
import com.example.mvcprac.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class ItemControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("상품등록 화면 보이는지 테스트")
    void itemForm() throws Exception {
        mockMvc.perform(get("/items/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("item/itemForm"))
                .andExpect(model().attributeExists("form"));
    }

}