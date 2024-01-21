package com.backend.dream.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class NotificationDTO {
    private Long id;

    private Long idAccount;
    private String notificationTitle;
    private String notificationText;

    private Timestamp createdTime;
    private long id_role;
    private String image;
}
