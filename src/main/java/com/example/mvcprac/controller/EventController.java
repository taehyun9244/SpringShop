package com.example.mvcprac.controller;

import com.example.mvcprac.dto.Event.EventForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Event;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.service.EventService;
import com.example.mvcprac.service.MeetingService;
import com.example.mvcprac.validation.CurrentAccount;
import com.example.mvcprac.validation.EventValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/meeting/{path}")
public class EventController {

    private final MeetingService meetingService;
    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @InitBinder("eventForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(eventValidator);
    }

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

    @GetMapping("/events/{id}")
    public String getEvent(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id,
                           Model model) {
        model.addAttribute(account);
        model.addAttribute(eventService.findById(id));
        model.addAttribute(meetingService.findMeetingWithMembersByPath(path));
        return "event/eventView";
    }

    @GetMapping("/events")
    public String viewStudyEvents(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeeting(path);
        model.addAttribute(account);
        model.addAttribute(meeting);

        List<Event> events = eventService.findByMeetingOrderByStartDateTime(meeting);
        List<Event> newEvents = new ArrayList<>();
        List<Event> oldEvents = new ArrayList<>();
        events.forEach(e -> {
            if (e.getEndDateTime().isBefore(LocalDateTime.now())) {
                oldEvents.add(e);
            } else {
                newEvents.add(e);
            }
        });

        model.addAttribute("newEvents", newEvents);
        model.addAttribute("oldEvents", oldEvents);

        return "meeting/events";
    }


}
