package com.example.mvcprac.service;

import com.example.mvcprac.dto.event.EventCreateDto;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Enrollment;
import com.example.mvcprac.model.Event;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.EnrollmentRepository;
import com.example.mvcprac.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EnrollmentRepository enrollmentRepository;


    public Event createEvent(Event event, Meeting meeting, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setMeeting(meeting);
        return eventRepository.save(event);
    }

    public Event findById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("can not find id")
        );
        return event;
    }

    public List<Event> findByMeetingOrderByStartDateTime(Meeting meeting) {
        List<Event> events = eventRepository.findByMeetingOrderByStartDateTime(meeting);
        return events;
    }

    public void updateEvent(Event event, EventCreateDto eventForm) {
        modelMapper.map(eventForm, event);
        event.acceptWaitingList();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public void newEnrollment(Event event, Account account) {

        if (!enrollmentRepository.existsByEventAndAccount(event, account)) {
            Enrollment enrollment = new Enrollment();
            enrollment.setEnrolledAt(LocalDateTime.now());
            enrollment.setAccepted(event.isAbleToAcceptWaitingEnrollment());
            enrollment.setAccount(account);
            event.addEnrollment(enrollment);
            enrollmentRepository.save(enrollment);
        }
    }

    public void cancelEnrollment(Event event, Account account) {
        Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);
        if (!enrollment.isAttended()) {
            event.removeEnrollment(enrollment);
            enrollmentRepository.delete(enrollment);
            event.acceptNextWaitingEnrollment();
        }
    }

    public void acceptEnrollment(Event event, Enrollment enrollment) {
        event.accept(enrollment);
    }

    public void rejectEnrollment(Event event, Enrollment enrollment) {
        event.reject(enrollment);
    }

    public void checkInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(true);
    }

    public void cancelCheckInEnrollment(Enrollment enrollment) {
        enrollment.setAttended(false);
    }
}
