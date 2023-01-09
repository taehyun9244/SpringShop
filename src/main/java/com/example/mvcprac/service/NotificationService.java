package com.example.mvcprac.service;

import com.example.mvcprac.model.Account;
import com.example.mvcprac.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Long countByAccountAndChecked(Account account, boolean checked) {
        long count = notificationRepository.countByAccountAndChecked(account, checked);
        return count;
    }
}
