package com.example.mvcprac.validation;

import com.example.mvcprac.dto.meeting.MeetingForm;
import com.example.mvcprac.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MeetingFormValidator implements Validator {

    private final MeetingRepository meetingRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MeetingForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeetingForm meetingForm = (MeetingForm) target;
        if (meetingRepository.existsByPath(meetingForm.getPath())) {
            errors.rejectValue("path", "wrong.path", "해당 교류 또는 모임 경로값을 사용할 수 없습니다.");
        }
    }
}
