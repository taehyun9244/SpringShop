package com.example.mvcprac.validation.validator;

import com.example.mvcprac.dto.meeting.MeetingCreateDto;
import com.example.mvcprac.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MeetingCreateDtoValidator implements Validator {

    private final MeetingRepository meetingRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MeetingCreateDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeetingCreateDto meetingCreateDto = (MeetingCreateDto) target;
        if (meetingRepository.existsByPath(meetingCreateDto.getPath())) {
            errors.rejectValue("path", "wrong.path", "해당 교류 또는 모임 경로값을 사용할 수 없습니다.");
        }
    }
}
