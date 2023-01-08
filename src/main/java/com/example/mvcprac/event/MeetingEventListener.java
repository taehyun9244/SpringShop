package com.example.mvcprac.event;

import com.example.mvcprac.mail.EmailMessage;
import com.example.mvcprac.mail.EmailService;
import com.example.mvcprac.model.Account;
import com.example.mvcprac.model.Meeting;
import com.example.mvcprac.model.Notification;
import com.example.mvcprac.repository.AccountRepository;
import com.example.mvcprac.repository.MeetingRepository;
import com.example.mvcprac.repository.NotificationRepository;
import com.example.mvcprac.repository.query.AccountPredicates;
import com.example.mvcprac.security.config.AppProperties;
import com.example.mvcprac.util.status.NotificationEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class MeetingEventListener {

    private final MeetingRepository meetingRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handlesMeetingCreatedEvent(MeetingCreateEvent meetingCreateEvent) {
        Meeting meeting = meetingRepository.findMeetingWithTagsAndZonesById(meetingCreateEvent.getMeeting().getId());
        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(meeting.getTags(), meeting.getZones()));
        accounts.forEach(account -> {
            if (account.isShopCreatedByEmail()) {
                sendMeetingCreatedEmail(meeting, account);
            }
            if (account.isShopCreatedByWeb()) {
                saveMeetingCreatedNotification(meeting, account);
            }
        });
    }

        private void saveMeetingCreatedNotification(Meeting meeting, Account account) {
            Notification notification = new Notification();
            notification.setTitle(meeting.getTitle());
            notification.setLink("/meeting/" + meeting.getEncodedPath());
            notification.setChecked(false);
            notification.setCreatedLocalDateTime(LocalDateTime.now());
            notification.setMessage(meeting.getShortDescription());
            notification.setAccount(account);
            notification.setNotificationEnum(NotificationEnum.MEETING_CREATED);
            notificationRepository.save(notification);
        }

        private void sendMeetingCreatedEmail(Meeting meeting, Account account) {
            Context context = new Context();

            context.setVariable("nickname", account.getNickname());
            context.setVariable("link", "/study/" + meeting.getEncodedPath());
            context.setVariable("linkName", meeting.getTitle());
            context.setVariable("message", "새로운 교류회가 생겼습니다");
            context.setVariable("host", appProperties.getHost());
            String message = templateEngine.process("mail/simple-link", context);

            EmailMessage emailMessage = EmailMessage.builder()
                    .subject("외국인 상품거래 서비스, '" + meeting.getTitle() + "' 교류회가 생겼습니다.")
                    .to(account.getEmail())
                    .message(message)
                    .build();

            emailService.sendEmail(emailMessage);

        }
}
