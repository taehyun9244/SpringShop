package com.example.mvcprac.controller;

import com.example.mvcprac.dto.event.EventCreateDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Enrollment;
import com.example.mvcprac.model.Event;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.service.EventService;
import com.example.mvcprac.service.MeetingService;
import com.example.mvcprac.validation.annotation.CurrentAccount;
import com.example.mvcprac.validation.validator.EventCreateDtoValidator;
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
    private final EventCreateDtoValidator eventCreateDtoValidator;

    @InitBinder("eventCreateDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(eventCreateDtoValidator);
    }

    @GetMapping("/new-event")
    public String newEventForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        model.addAttribute(meeting);
        model.addAttribute(account);
        model.addAttribute(new EventCreateDto());
        return "event/eventForm";
    }

    @PostMapping("/new-event")
    public String newEventSubmit(@CurrentAccount Account account, @PathVariable String path, @Validated EventCreateDto eventCreateDto,
                                 BindingResult bindingResult, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdateStatus(account, path);
        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(meeting);
            return "event/eventForm";
        }

        Event event = eventService.createEvent(modelMapper.map(eventCreateDto, Event.class), meeting, account);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{id}")
    public String getEvent(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id, Model model) {
        model.addAttribute(account);
        model.addAttribute(eventService.findById(id));
        model.addAttribute(meetingService.findMeetingWithMembersByPath(path));
        return "event/eventView";

        //TODO 현재 refactoring 한 결과, 참가 신청 후 view가 제대로 나오지 않음
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

    @GetMapping("/events/{id}/edit")
    public String updateEventForm(@CurrentAccount Account account, @PathVariable Long id, @PathVariable String path, Model model) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        Event event = eventService.findById(id);
        model.addAttribute(meeting);
        model.addAttribute(account);
        model.addAttribute(event);
        model.addAttribute(modelMapper.map(event, EventCreateDto.class));
        return "event/eventEdit";
    }

    @PostMapping("/events/{id}/edit")
    public String updateEventSubmit(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id,
                                    @Validated EventCreateDto eventCreateDto, BindingResult bindingResult, Model model) {

        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        Event event = eventService.findById(id);
        eventCreateDto.setEventEunm(event.getEventEunm());
        eventCreateDtoValidator.validateUpdateForm(eventCreateDto, event, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(meeting);
            model.addAttribute(event);
            return "event/eventForm";
        }

        eventService.updateEvent(event, eventCreateDto);
        return "redirect:/meeting/" + meeting.getEncodedPath() +  "/events/" + event.getId();
    }

    @PostMapping("/events/{id}/delete")
    public String cancelEvent(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id) {
        Meeting meeting  = meetingService.getMeetingToUpdateStatus(account, path);
        eventService.deleteEvent(eventService.findById(id));
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/events/";
    }

    @PostMapping("/events/{id}/enroll")
    public String newEnrollment(@CurrentAccount Account account,
                                @PathVariable String path, @PathVariable Long id) {
        Meeting meeting = meetingService.getMeetingToEnroll(path);
        Event event = eventService.findById(id);
        eventService.newEnrollment(event, account);
        return "redirect:/meeting/" + meeting.getEncodedPath() +  "/events/" + id;
    }

    @PostMapping("/events/{id}/disenroll")
    public String cancelEnrollment(@CurrentAccount Account account,
                                   @PathVariable String path, @PathVariable Long id) {
        Meeting meeting = meetingService.getMeetingToEnroll(path);
        Event event = eventService.findById(id);
        eventService.cancelEnrollment(event, account);
        return "redirect:/meeting/" + meeting.getEncodedPath() +  "/events/" + id;
    }

    @GetMapping("events/{eventId}/enrollments/{enrollmentId}/accept")
    public String acceptEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                   @PathVariable("eventId") Event event, @PathVariable("enrollmentId") Enrollment enrollment) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        eventService.acceptEnrollment(event, enrollment);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/reject")
    public String rejectEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                   @PathVariable("eventId") Event event, @PathVariable("enrollmentId") Enrollment enrollment) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        eventService.rejectEnrollment(event, enrollment);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/checkin")
    public String checkInEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                    @PathVariable("eventId") Event event, @PathVariable("enrollmentId") Enrollment enrollment) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        eventService.checkInEnrollment(enrollment);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/cancel-checkin")
    public String cancelCheckInEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                          @PathVariable("eventId") Event event, @PathVariable("enrollmentId") Enrollment enrollment) {
        Meeting meeting = meetingService.getMeetingToUpdate(account, path);
        eventService.cancelCheckInEnrollment(enrollment);
        return "redirect:/meeting/" + meeting.getEncodedPath() + "/events/" + event.getId();
    }

}
