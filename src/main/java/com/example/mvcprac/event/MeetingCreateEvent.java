package com.example.mvcprac.event;

import com.example.mvcprac.model.Meeting;
import lombok.Getter;

@Getter
public class MeetingCreateEvent {

    private Meeting meeting;
    public MeetingCreateEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}
