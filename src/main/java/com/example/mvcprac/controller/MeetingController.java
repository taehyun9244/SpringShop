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
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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

    @GetMapping("/meetings/{path}")
    public String meetingHomeView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        model.addAttribute(account);
        model.addAttribute(meetingService.findByPath(path));
        return "meeting/meeting-home";
    }

    @GetMapping("/new-meeting")
    public String meetingFormView(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new MeetingForm());
        return "/meeting/meetingForm";
    }

    @PostMapping("/new-meeting")
    public String newMeetingSubmit(@CurrentAccount Account account, @Valid MeetingForm meetingForm, Errors errors) {
        if (errors.hasErrors()) {
            return "meeting/meetingForm";
        }

        Meeting newMeeting = meetingService.createNewMeeting(modelMapper.map(meetingForm, Meeting.class), account);
        return "redirect:/meeting/" + URLEncoder.encode(newMeeting.getPath(), StandardCharsets.UTF_8);
    }
}
