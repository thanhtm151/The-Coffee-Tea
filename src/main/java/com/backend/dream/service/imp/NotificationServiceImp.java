package com.backend.dream.service.imp;

import com.backend.dream.dto.NotificationDTO;
import com.backend.dream.entity.Account;
import com.backend.dream.entity.Notification;
import com.backend.dream.entity.Role;
import com.backend.dream.mapper.NotificationMapper;
import com.backend.dream.repository.AccountRepository;
import com.backend.dream.repository.NotificationRepository;
import com.backend.dream.repository.RoleRepository;
import com.backend.dream.service.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Transactional
@Service
public class NotificationServiceImp implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public NotificationServiceImp(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }
    @Override
    public List<NotificationDTO> getNotificationsByAccountId(Long idAccount) {
        List<Notification> notifications = notificationRepository.findAllNotifications(idAccount);
        return notifications.stream()
                .map(notificationMapper::notificationToNotificationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void createNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationMapper.notificationDTOToNotification(notificationDTO);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDTO> getAdminNotifications() {
        List<Notification> notifications = notificationRepository.findAdminNotifications();
        return notifications.stream()
                .map(notificationMapper::notificationToNotificationDTO)
                .collect(Collectors.toList());
    }
}
