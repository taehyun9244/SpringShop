package com.example.mvcprac.controller;

import com.example.mvcprac.dto.account.NicknameForm;
import com.example.mvcprac.dto.account.PasswordForm;
import com.example.mvcprac.dto.profile.Notifications;
import com.example.mvcprac.dto.profile.Profile;
import com.example.mvcprac.dto.tag.TagForm;
import com.example.mvcprac.dto.zone.ZoneForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.model.Zone;
import com.example.mvcprac.service.AccountService;
import com.example.mvcprac.service.SettingService;
import com.example.mvcprac.validation.customize.CurrentAccount;
import com.example.mvcprac.validation.validator.NicknameValidator;
import com.example.mvcprac.validation.validator.PasswordFormValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

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

    private final NicknameValidator nicknameValidator;
    private final SettingService settingService;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
        public void passwordFormInitBinder(WebDataBinder webDataBinder) {
            webDataBinder.addValidators(new PasswordFormValidator());
        }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
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
    public String updateProfile(@CurrentAccount Account account, @Valid Profile profile, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PROFILE;
        }

        accountService.updateProfile(account, profile);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정했습니다");
        return "redirect:/" + SETTINGS + PROFILE;
    }

    /**
     * Change Password
     */
    @GetMapping(PASSWORD)
    public String updatePasswordForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS + PASSWORD;
    }

    @PostMapping(PASSWORD)
    public String updatePassword(@CurrentAccount Account account, @Valid PasswordForm passwordForm, BindingResult bindingResult,
                                 Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PASSWORD;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
        return "redirect:/" + SETTINGS + PASSWORD;
    }

    /**
     * update Notifications
     */
    @GetMapping(NOTIFICATIONS)
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));
        return SETTINGS + NOTIFICATIONS;
    }

    @PostMapping(NOTIFICATIONS)
    public String updateNotifications(@CurrentAccount Account account, @Valid Notifications notifications, BindingResult bindingResult,
                                      Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + NOTIFICATIONS;
        }

        accountService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:/" + SETTINGS + NOTIFICATIONS;
    }

    /**
     * Change Nickname
     */
    @GetMapping(ACCOUNT)
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));
        return SETTINGS + ACCOUNT;
    }

    @PostMapping(ACCOUNT)
    public String updateAccount(@CurrentAccount Account account, @Valid NicknameForm nicknameForm, BindingResult bindingResult,
                                Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + ACCOUNT;
        }

        accountService.updateNickname(account, nicknameForm.getNickname());
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
    public ResponseEntity updateTags(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        Tag tag = settingService.findTagTitle(tagForm);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping(TAGS + "/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        Tag tag = settingService.removeTag(tagForm);
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
    public ResponseEntity updateZones(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = settingService.addZone(zoneForm);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account, zone);
        return ResponseEntity.ok().build();
    }
    @PostMapping(ZONES + "/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = settingService.removeZone(zoneForm);
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);
        return ResponseEntity.ok().build();
    }

}
