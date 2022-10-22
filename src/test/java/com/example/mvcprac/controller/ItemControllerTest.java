package com.example.mvcprac.controller;

import com.example.mvcprac.dto.account.SignUpForm;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.repository.ItemRepository;
import com.example.mvcprac.service.AccountService;
import com.example.mvcprac.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class ItemControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm(
                "남태값",
                "12341234!a",
                "19920404",
                "시모키타자와",
                "email@email.com",
                "서울",
                "01012345678");
        accountService.createUser(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("상품등록 view")
    void itemForm_view() throws Exception {
        mockMvc.perform(get("/items/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("item/itemForm"))
                .andExpect(model().attributeExists("form"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("상품등록 실패 - null 값")
    void itemForm_fail() throws Exception {
        mockMvc.perform(post("/items/add")
                        .param("itemName", "")
                        .param("itemBody", "")
                        .param("price", "10000")
                        .param("itemSellStatus", "SELL")
                        .param("deliveryChoice", "POST")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("item/itemForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("form"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("상품등록 성공")
    void itemForm_success() throws Exception {
        mockMvc.perform(post("/items/add")
                        .param("itemName", "macbook")
                        .param("itemBody", "new")
                        .param("price", "10000")
                        .param("itemSellStatus", "SELL")
                        .param("deliveryChoice", "POST")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/{id}"))
                .andExpect(model().attributeExists("form"));

//        Optional<Item> byId = itemRepository.findById();
//        Assertions.assertTrue(passwordEncoder.matches("12345678@a", byEmail.getPassword()));
    }

}