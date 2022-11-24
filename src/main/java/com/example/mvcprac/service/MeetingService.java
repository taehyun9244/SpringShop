package com.example.mvcprac.service;

import com.example.mvcprac.dto.meeting.MeetingForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;


    public Meeting createNewMeeting(MeetingForm meetingForm, Account account) {
        Meeting newMeeting = new Meeting(meetingForm);
        Meeting saveMeeting = meetingRepository.save(newMeeting);
        newMeeting.addManager(account);
        return saveMeeting;
    }

    public Meeting findByPath(String path) {
        Meeting meetingByPath = meetingRepository.findByPath(path);
        return meetingByPath;
    }
}
