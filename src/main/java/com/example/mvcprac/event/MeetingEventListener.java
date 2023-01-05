package com.example.mvcprac.event;

import com.example.mvcprac.model.Meeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Component
@Transactional(readOnly = true)
public class MeetingEventListener {

    @EventListener
    public void handlesMeetingCreatedEvent(MeetingCreateEvent meetingCreateEvent) {
        Meeting meeting = meetingCreateEvent.getMeeting();
        log.info("meeting created = {}", meeting.getTitle());
        // TODO send email or, save Notification of DB
        throw new RuntimeException();
    }
}
