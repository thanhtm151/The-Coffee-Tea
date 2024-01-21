package com.backend.dream.service;

import com.backend.dream.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsByAccountId(Long idAccount);

    void createNotification(NotificationDTO notificationDTO);

    List<NotificationDTO> getAdminNotifications();
}
