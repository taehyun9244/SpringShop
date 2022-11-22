package com.example.mvcprac.service;

import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingService {

    private final MeetingRepository meetingRepository;


    public Meeting createNewMeeting(Meeting meeting, Account account) {
        Meeting newMeeting = meetingRepository.save(meeting);
        newMeeting.addManager(account);
        return newMeeting;
    }

    public Meeting findByPath(String path) {
        Meeting meetingByPath = meetingRepository.findByPath(path);
        return meetingByPath;
    }
}
