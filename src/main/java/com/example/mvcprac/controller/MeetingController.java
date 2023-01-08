package com.example.mvcprac.controller;

import com.example.mvcprac.dto.meeting.MeetingCreateDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.service.MeetingService;
import com.example.mvcprac.validation.annotation.CurrentAccount;
import com.example.mvcprac.validation.validator.MeetingCreateDtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final MeetingCreateDtoValidator meetingCreateDtoValidator;

    @InitBinder("meetingCreateDto")
    public void meetingFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(meetingCreateDtoValidator);
    }

    @GetMapping("/new-meeting")
    public String meetingFormView(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new MeetingCreateDto());
        return "meeting/meetingForm";
    }

    @PostMapping("/new-meeting")
    public String newMeetingSubmit(@Valid @ModelAttribute MeetingCreateDto meetingCreateDto, BindingResult bindingResult,
                                   @CurrentAccount Account account, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            model.addAttribute(account);
            return "meeting/meetingForm";
        }
        Meeting newMeeting = meetingService.createNewMeeting(meetingCreateDto, account);
        return "redirect:/meeting/" + URLEncoder.encode(newMeeting.getPath(), StandardCharsets.UTF_8);
    }

    @GetMapping("/meeting/{path}")
    public String meetingHomeView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.findByPath(path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        return "meeting/meetingView";
    }

    @GetMapping("/meeting/{path}/members")
    public String viewStudyMembers(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.findByPath(path);
        model.addAttribute(account);
        model.addAttribute(meeting);
        return "meeting/meetingMember";
    }

    @GetMapping("/meeting/{path}/join")
    public String joinMeeting(@CurrentAccount Account account, @PathVariable String path) {
        Meeting meeting = meetingService.findMeetingWithMembersByPath(path);
        meetingService.addMember(meeting, account);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/members";
    }

    @GetMapping("/meeting/{path}/leave")
    public String leaveMeeting(@CurrentAccount Account account, @PathVariable String path) {
        Meeting meeting = meetingService.findMeetingWithMembersByPath(path);
        meetingService.removeMember(meeting, account);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/members";
    }
}
