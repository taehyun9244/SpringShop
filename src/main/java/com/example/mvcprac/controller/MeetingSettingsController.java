package com.example.mvcprac.controller;

import com.example.mvcprac.dto.meeting.MeetingDescriptionForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.service.MeetingService;
import com.example.mvcprac.validation.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/meeting/{path}/settings")
@RequiredArgsConstructor
public class MeetingSettingsController {

    private final MeetingService meetingService;
    private final ModelMapper modelMapper;

    @GetMapping("/description")
    public String viewMeetingSetting(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        model.addAttribute(modelMapper.map(meeting, MeetingDescriptionForm.class));
        return "meeting/settings/description";
    }

    @PostMapping("/description")
    public String updateMeetingInfo(@CurrentAccount Account account, @PathVariable String path,
                                    @Validated MeetingDescriptionForm studyDescriptionForm, BindingResult bindingResult,
                                    Model model, RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);

        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(meeting);
            return "meeting/settings/description";
        }

        meetingService.updateMeetingDescription(meeting, studyDescriptionForm);
        attributes.addFlashAttribute("message", "교류회 소개를 수정했습니다.");
        return "redirect:/meeting/" + getPath(path) + "/settings/description";
    }

    @GetMapping("/banner")
    public String meetingBannerForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        model.addAttribute(modelMapper.map(meeting, MeetingDescriptionForm.class));
        return "meeting/settings/banner";
    }

    @PostMapping("/banner")
    public String meetingBannerSubmit(@CurrentAccount Account account, @PathVariable String path,
                                   String image, RedirectAttributes attributes) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        meetingService.updateMeetingImage(meeting, image);
        attributes.addFlashAttribute("message", "교류회 이미지를 수정했습니다.");
        return "redirect:/meeting/" + getPath(path) + "/settings/banner";
    }

    private String getPath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    @PostMapping("/banner/enable")
    public String enableMeetingBanner(@CurrentAccount Account account, @PathVariable String path) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        meetingService.enableMeetingBanner(meeting);
        return "redirect:/meeting/" + getPath(path) + "/settings/banner";
    }

    @PostMapping("/banner/disable")
    public String disableStudyBanner(@CurrentAccount Account account, @PathVariable String path) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        meetingService.disableMeetingBanner(meeting);
        return "redirect:/meeting/" + getPath(path) + "/settings/banner";
    }

}
