package com.example.mvcprac.service;

import com.example.mvcprac.dto.meeting.MeetingDescriptionForm;
import com.example.mvcprac.dto.meeting.MeetingForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ModelMapper modelMapper;


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

    public Meeting getMeetingToUpdate(Account account, String path) {
        Meeting meeting = this.getMeeting(path);
        if (!account.isManagerOf(meeting)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }

        return meeting;
    }

    public Meeting getMeeting(String path) {
        Meeting meeting = this.meetingRepository.findByPath(path);
        if (meeting == null) {
            throw new IllegalArgumentException(path + "에 해당하는 교류회가 없습니다.");
        }

        return meeting;
    }

    public void updateMeetingDescription(Meeting meeting, MeetingDescriptionForm meetingDescriptionForm) {
        modelMapper.map(meetingDescriptionForm, meeting);
    }

    public void updateMeetingImage(Meeting meeting, String image) {
        meeting.setImage(image);
    }


    public void enableMeetingBanner(Meeting meeting) {
        meeting.setUseBanner(true);
    }

    public void disableMeetingBanner(Meeting meeting) {
        meeting.setUseBanner(false);
    }
}
