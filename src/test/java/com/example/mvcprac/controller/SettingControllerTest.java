package com.example.mvcprac.controller;

import com.example.mvcprac.MockMvcTest;
import com.example.mvcprac.dto.account.SignUpDto;
import com.example.mvcprac.dto.tag.TagRegisterDto;
import com.example.mvcprac.dto.zone.ZoneRegisterDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.model.Zone;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.repository.TagRepository;
import com.example.mvcprac.repository.ZoneRepository;
import com.example.mvcprac.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.mvcprac.controller.SettingController.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class SettingControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ZoneRepository zoneRepository;

    private Zone testZone = new Zone("testCity", "테스트시", "테스트주");

    @BeforeEach
    void beforeEach() {
        SignUpDto signUpDto= new SignUpDto(
                "남태현",
                "12341234!a",
                "19920404",
                "시모키타자와",
                "email@email.com",
                "서울",
                "01012345678");
        accountService.createUser(signUpDto);

        zoneRepository.save(testZone);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
        zoneRepository.deleteAll();
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @DisplayName("계정의 지역 정보 수정 폼")
    @Test
    void updateZonesForm() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + ZONES))
                .andExpect(view().name(SETTINGS + ZONES))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("zones"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @DisplayName("계정의 지역 정보 add")
    @Test
    void addZone() throws Exception {
        ZoneRegisterDto ZoneRegisterDto = new ZoneRegisterDto();
        ZoneRegisterDto.setZoneName(testZone.toString());

        mockMvc.perform(post(ROOT + SETTINGS + ZONES + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ZoneRegisterDto))
                        .with(csrf()))
                .andExpect(status().isOk());

        Account simokitazawa = accountRepository.findByPhoneNumber("01012345678");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        assertTrue(simokitazawa.getZones().contains(zone));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @DisplayName("계정의 지역 정보 remove")
    @Test
    void removeZone() throws Exception {
        Account simokitazawa = accountRepository.findByPhoneNumber("01012345678");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        accountService.addZone(simokitazawa, zone);

        ZoneRegisterDto ZoneRegisterDto = new ZoneRegisterDto();
        ZoneRegisterDto.setZoneName(testZone.toString());

        mockMvc.perform(post(ROOT + SETTINGS + ZONES + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ZoneRegisterDto))
                        .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(simokitazawa.getZones().contains(zone));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("프로필 수정하기 view")
    void updateProfileView() throws Exception{
        mockMvc.perform(get(ROOT + SETTINGS + PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profileDto"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("프로필 수정하기 - 입력값 정상")
    void updateProfile() throws Exception{
        String bio = "수정하는 bio";
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + PROFILE))
                .andExpect(flash().attributeExists("message"));

        Account simokitazawa = accountRepository.findByNickname("시모키타자와");
        assertThat(bio).isEqualTo(simokitazawa.getBio());
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("프로필 수정하기 - 입력값 에러")
    void updateProfile_error() throws Exception{
        String bio = "35자가 넘어가면 에러가 나기 때문에 오류가 테스트 코드는 실패해야만 한다. 자 그럼 이제 테스트코드를 실행해 보겠다";
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PROFILE))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profileDto"))
                .andExpect(model().hasErrors());

        Account simokitazawa = accountRepository.findByNickname("시모키타자와");
        Assertions.assertNull(simokitazawa.getBio());
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("password 수정하기 view")
    void updatePassword_view() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordDto"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("password 수정하기 - 입력값 에러 - 불일치")
    void updatePassword_fail() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
                        .param("newPassword", "12345678@a")
                        .param("newPasswordConfirm", "11111111@a")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PASSWORD))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordDto"))
                .andExpect(model().attributeExists("account"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("password 수정하기 - 입력값 정상")
    void updatePassword_success() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
                        .param("newPassword", "12345678@a")
                        .param("newPasswordConfirm", "12345678@a")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + PASSWORD))
                .andExpect(flash().attributeExists("message"));

        Account byEmail = accountRepository.findByEmail("email@email.com");
        Assertions.assertTrue(passwordEncoder.matches("12345678@a", byEmail.getPassword()));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("nickname 수정하기 view")
    void updateNickname_view() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + ACCOUNT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameDto"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("nickname 수정하기 - 입력값 에러 - 불일치")
    void updateNickname_fail() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + ACCOUNT)
                        .param("nickname", "이것은 엄청길게 쓴 것 입니다 30자가넘으면 안되는데 그냥 넘기고 에러를 읽으킨다")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + ACCOUNT))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("nicknameDto"))
                .andExpect(model().attributeExists("account"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("nickname 수정하기 - 입력값 정상")
    void updateNickname_success() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + ACCOUNT)
                        .param("nickname", "시모키타자와44")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + ACCOUNT))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickname("시모키타자와44"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("계정의 Tag 수정 form")
    void updateTags_view() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + TAGS))
                .andExpect(view().name(SETTINGS + TAGS))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("계정의 Tag add")
    void addTag() throws Exception {

        TagRegisterDto tagForm = new TagRegisterDto();
        tagForm.setTagTitle("newTagTitle");

        mockMvc.perform(post(ROOT + SETTINGS + TAGS + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Tag newTagTitle = tagRepository.findByTitle("newTagTitle");
        assertNotNull(newTagTitle);
        assertTrue(accountRepository.findByPhoneNumber("01012345678").getTags().contains(newTagTitle));
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION) // before 실행 후 test code 실행 직전에 실행해라
    @Test
    @DisplayName("계정의 Tag remove")
    void removeTag() throws Exception {

        Account simokitazawa = accountRepository.findByPhoneNumber("01012345678");

        TagRegisterDto tagForm = new TagRegisterDto();
        tagForm.setTagTitle("newTagTitle");
        Tag saveTag = new Tag(tagForm.getTagTitle());
        Tag newTag = tagRepository.save(saveTag);

        accountService.addTag(simokitazawa, newTag);

        assertTrue(simokitazawa.getTags().contains(newTag));

        mockMvc.perform(post(ROOT + SETTINGS + TAGS + "/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(simokitazawa.getTags().contains(newTag));
    }
}