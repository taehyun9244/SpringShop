package com.example.mvcprac.service;

import com.example.mvcprac.dto.meeting.MeetingDescriptionForm;
import com.example.mvcprac.dto.meeting.MeetingForm;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.model.Zone;
import com.example.mvcprac.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.mvcprac.dto.meeting.MeetingForm.VALID_PATH_PATTERN;

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
        checkIfManager(account, meeting);
        return meeting;
    }

    public Meeting getMeeting(String path) {
        Meeting meeting = this.meetingRepository.findByPath(path);
        checkIfExistingStudy(path, meeting);
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

    public void addTag(Meeting meeting, Tag tag) {
        meeting.getTags().add(tag);
    }

    public void removeTag(Meeting meeting, Tag tag) {
        meeting.getTags().remove(tag);
    }

    public void addZone(Meeting meeting, Zone zone) {
        meeting.getZones().add(zone);
    }

    public void removeZone(Meeting meeting, Zone zone) {
        meeting.getZones().remove(zone);
    }

    public Meeting getMeetingToUpdateTag(Account account, String path) {
        Meeting meeting = meetingRepository.findMeetingWithTagsByPath(path);
        checkIfExistingStudy(path, meeting);
        checkIfManager(account, meeting);
        return meeting;
    }

    public Meeting getMeetingToUpdateZone(Account account, String path) {
        Meeting meeting = meetingRepository.findMeetingWithZonesByPath(path);
        checkIfExistingStudy(path, meeting);
        checkIfManager(account, meeting);
        return meeting;
    }

    private void checkIfManager(Account account, Meeting meeting) {
        if (!account.isManagerOf(meeting)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    private void checkIfExistingStudy(String path, Meeting meeting) {
        if (meeting == null) {
            throw new IllegalArgumentException(path + "에 해당하는 교류회가 없습니다.");
        }
    }

    public Meeting getMeetingToUpdateStatus(Account account, String path) {
        Meeting meeting = meetingRepository.findMeetingWithManagersByPath(path);
        checkIfExistingStudy(path, meeting);
        checkIfManager(account, meeting);
        return meeting;
    }

    public void publish(Meeting meeting) {
        meeting.publish();
    }

    public void close(Meeting meeting) {
        meeting.close();
    }

    public void startRecruit(Meeting meeting) {
        meeting.startRecruit();
    }

    public void stopRecruit(Meeting meeting) {
        meeting.stopRecruit();
    }

    public boolean isValidPath(String newPath) {
        if (!newPath.matches(VALID_PATH_PATTERN)) {
            return false;
        }

        return !meetingRepository.existsByPath(newPath);
    }

    public void updateMeetingPath(Meeting meeting, String newPath) {
        meeting.setPath(newPath);
    }

    public boolean isValidTitle(String newTitle) {
        return newTitle.length() <= 50;
    }

    public void updateMeetingTitle(Meeting meeting, String newTitle) {
        meeting.setTitle(newTitle);
    }

    public void remove(Meeting meeting) {
        if (meeting.isRemovable()) {
            meetingRepository.delete(meeting);
        } else {
            throw new IllegalArgumentException("교류회를 삭제할 수 없습니다.");
        }
    }

    public void addMember(Meeting meeting, Account account) {
        meeting.addMember(account);
    }

    public void removeMember(Meeting meeting, Account account) {
        meeting.removeMember(account);
    }

    public Meeting findMeetingWithMembersByPath(String path) {
        Meeting meeting = meetingRepository.findMeetingWithMembersByPath(path);
        return meeting;
    }

    public Meeting getMeetingToEnroll(String path) {
        Meeting meeting = meetingRepository.findMeetingOnlyByPath(path);
        checkIfExistingStudy(path, meeting);
        return meeting;
    }
}
