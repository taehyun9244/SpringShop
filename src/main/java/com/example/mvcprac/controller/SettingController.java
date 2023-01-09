package com.example.mvcprac.controller;

import com.example.mvcprac.dto.profile.NicknameDto;
import com.example.mvcprac.dto.profile.PasswordDto;
import com.example.mvcprac.dto.profile.NotificationsDto;
import com.example.mvcprac.dto.profile.Profile;
import com.example.mvcprac.dto.tag.TagRegisterDto;
import com.example.mvcprac.dto.zone.ZoneRegisterDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.model.Zone;
import com.example.mvcprac.service.AccountService;
import com.example.mvcprac.service.SettingService;
import com.example.mvcprac.validation.annotation.CurrentAccount;
import com.example.mvcprac.validation.validator.NicknameDtoValidator;
import com.example.mvcprac.validation.validator.PasswordDtoValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.mvcprac.controller.SettingController.ROOT;
import static com.example.mvcprac.controller.SettingController.SETTINGS;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT + SETTINGS)
public class SettingController {

    static final String ROOT = "/";
    static final String SETTINGS = "settings";
    static final String PROFILE = "/profile";
    static final String PASSWORD = "/password";
    static final String NOTIFICATIONS = "/notifications";
    static final String ACCOUNT = "/account";
    static final String TAGS = "/tags";
    static final String ZONES = "/zones";

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    private final NicknameDtoValidator nicknameDtoValidator;
    private final SettingService settingService;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordDto")
        public void passwordFormInitBinder(WebDataBinder webDataBinder) {
            webDataBinder.addValidators(new PasswordDtoValidator());
        }

    @InitBinder("nicknameDto")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameDtoValidator);
    }

    /**
     * update Profile
     */
    @GetMapping(PROFILE)
    public String profileUpdateForm(@CurrentAccount Account account, Model model) {

        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS + PROFILE;

    }

    @PostMapping(PROFILE)
    public String updateProfile(@CurrentAccount Account account, @Validated Profile profile, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PROFILE;
        }

        accountService.updateProfile(account, profile);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정했습니다");
        return "redirect:/" + SETTINGS + PROFILE;

        //TODO 현재 refactoring 한 결과, 프로필 사진 업로드 및 수정이 불가
    }

    /**
     * Change Password
     */
    @GetMapping(PASSWORD)
    public String updatePasswordForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordDto());
        return SETTINGS + PASSWORD;
    }

    @PostMapping(PASSWORD)
    public String updatePassword(@CurrentAccount Account account, @Validated PasswordDto passwordDto, BindingResult bindingResult,
                                 Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PASSWORD;
        }

        accountService.updatePassword(account, passwordDto.getNewPassword());
        attributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
        return "redirect:/" + SETTINGS + PASSWORD;
    }

    /**
     * update Notifications
     */
    @GetMapping(NOTIFICATIONS)
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NotificationsDto.class));
        return SETTINGS + NOTIFICATIONS;
    }

    @PostMapping(NOTIFICATIONS)
    public String updateNotifications(@CurrentAccount Account account, @Validated NotificationsDto notificationsDto, BindingResult bindingResult,
                                      Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + NOTIFICATIONS;
        }

        accountService.updateNotifications(account, notificationsDto);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:/" + SETTINGS + NOTIFICATIONS;
    }

    /**
     * Change Nickname
     */
    @GetMapping(ACCOUNT)
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameDto.class));
        return SETTINGS + ACCOUNT;
    }

    @PostMapping(ACCOUNT)
    public String updateAccount(@CurrentAccount Account account, @Validated NicknameDto nicknameDto, BindingResult bindingResult,
                                Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + ACCOUNT;
        }

        accountService.updateNickname(account, nicknameDto.getNickname());
        attributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
        return "redirect:/" + SETTINGS + ACCOUNT;
    }


    /**
     * Tags
     */
    @GetMapping(TAGS)
    public String updateTags(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTags = settingService.findAllTagTitle();
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

        return SETTINGS + TAGS;
    }

    @PostMapping(TAGS + "/add")
    @ResponseBody
    public ResponseEntity updateTags(@CurrentAccount Account account, @RequestBody TagRegisterDto tagRegisterDto) {
        Tag tag = settingService.findTagTitle(tagRegisterDto);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping(TAGS + "/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentAccount Account account, @RequestBody TagRegisterDto tagRegisterDto) {
        Tag tag = settingService.removeTag(tagRegisterDto);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account, tag);
        return ResponseEntity.ok().build();
    }

    /**
     * Zones
     */
    @GetMapping(ZONES)
    public String updateZones(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Zone> zones = accountService.getZones(account);
        model.addAttribute("zones", zones.stream().map(Zone::toString).collect(Collectors.toList()));

        List<String> allZones = settingService.findAllZones();
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));

        return SETTINGS + ZONES;
    }

    @PostMapping(ZONES + "/add")
    @ResponseBody
    public ResponseEntity updateZones(@CurrentAccount Account account, @RequestBody ZoneRegisterDto zoneRegisterDto) {
        Zone zone = settingService.addZone(zoneRegisterDto);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account, zone);
        return ResponseEntity.ok().build();
    }
    @PostMapping(ZONES + "/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentAccount Account account, @RequestBody ZoneRegisterDto zoneRegisterDto) {
        Zone zone = settingService.removeZone(zoneRegisterDto);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);
        return ResponseEntity.ok().build();
    }

}
