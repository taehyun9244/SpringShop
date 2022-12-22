package com.example.mvcprac.controller;

import com.example.mvcprac.dto.Event.EventForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Event;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.service.EventService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/meeting/{path}")
public class EventController {

    private final MeetingService meetingService;
    private final EventService eventService;
    private final ModelMapper modelMapper;

    @GetMapping("/new-event")
    public String newEventForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        model.addAttribute(meeting);
        model.addAttribute(account);
        model.addAttribute(new EventForm());
        return "event/eventForm";
    }

    @PostMapping("/new-event")
    public String newEventSubmit(@CurrentAccount Account account, @PathVariable String path,
                                 @Validated EventForm eventForm, BindingResult bindingResult, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(meeting);
            return "event/eventForm";
        }

        Event event = eventService.createEvent(modelMapper.map(eventForm, Event.class), meeting, account);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/events/" + event.getId();
    }

}
