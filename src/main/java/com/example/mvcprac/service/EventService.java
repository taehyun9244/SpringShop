package com.example.mvcprac.service;

import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Event;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;


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
}
