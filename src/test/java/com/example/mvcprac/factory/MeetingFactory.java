package com.example.mvcprac.factory;

import com.example.mvcprac.dto.meeting.MeetingForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingFactory {
    @Autowired
    MeetingService meetingService;

    public Meeting createMeeting(String path, Account manager) {
        MeetingForm meeting = new MeetingForm(
                path, "testTile", "TestShortDescription", "TestFullDescription"
        );
        Meeting newMeeting = meetingService.createNewMeeting(meeting, manager);
        return newMeeting;
    }
}
