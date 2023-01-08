package com.example.mvcprac.validation.validator;

import com.example.mvcprac.dto.event.EventCreateDto;
import com.example.mvcprac.model.Event;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventCreateDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EventCreateDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventCreateDto eventCreateDto = (EventCreateDto)target;

        if (isNotValidEndEnrollmentDateTime(eventCreateDto)) {
            errors.rejectValue("endEnrollmentDateTime", "wrong.datetime", "모임 접수 종료 일시를 정확히 입력하세요.");
        }

        if (isNotValidEndDateTime(eventCreateDto)) {
            errors.rejectValue("endDateTime", "wrong.datetime", "모임 종료 일시를 정확히 입력하세요.");
        }

        if (isNotValidStartDateTime(eventCreateDto)) {
            errors.rejectValue("startDateTime", "wrong.datetime", "모임 시작 일시를 정확히 입력하세요.");
        }
    }

    private boolean isNotValidStartDateTime(EventCreateDto eventCreateDto) {
        return eventCreateDto.getStartDateTime().isBefore(eventCreateDto.getEndEnrollmentDateTime());
    }

    private boolean isNotValidEndEnrollmentDateTime(EventCreateDto eventForm) {
        return eventForm.getEndEnrollmentDateTime().isBefore(LocalDateTime.now());
    }

    private boolean isNotValidEndDateTime(EventCreateDto eventForm) {
        LocalDateTime endDateTime = eventForm.getEndDateTime();
        return endDateTime.isBefore(eventForm.getStartDateTime()) || endDateTime.isBefore(eventForm.getEndEnrollmentDateTime());
    }

    public void validateUpdateForm(EventCreateDto eventForm, Event event, BindingResult bindingResult) {
        if (eventForm.getLimitOfEnrollments() < event.getNumberOfAcceptedEnrollments()) {
            bindingResult.rejectValue("limitOfEnrollments", "wrong.value", "확인된 참기 신청보다 모집 인원 수가 커야 합니다.");
        }
    }
}
