package com.example.mvcprac.controller;

import com.example.mvcprac.dto.meeting.MeetingForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.service.MeetingService;
import com.example.mvcprac.validation.CurrentAccount;
import com.example.mvcprac.validation.MeetingFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final ModelMapper modelMapper;
    private final MeetingFormValidator meetingFormValidator;

    @InitBinder("meetingForm")
    public void studyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(meetingFormValidator);
    }

    @GetMapping("/meeting/{path}")
    public String meetingHomeView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        log.info("path", path);
        model.addAttribute(account);
        model.addAttribute(meetingService.findByPath(path));
        return "meeting/meetingView";
    }

    @GetMapping("/new-meeting")
    public String meetingFormView(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new MeetingForm());
        return "/meeting/meetingForm";
    }

    @PostMapping("/new-meeting")
    public String newMeetingSubmit(@Validated @ModelAttribute(name = "meetingForm") MeetingForm meetingForm,
                                   @CurrentAccount Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "meeting/meetingForm";
        }
        log.info("form.path", meetingForm.getPath());
        Meeting newMeeting = meetingService.createNewMeeting(meetingForm, account);
        return "redirect:/meeting/" + URLEncoder.encode(newMeeting.getPath(), StandardCharsets.UTF_8);
    }
}
