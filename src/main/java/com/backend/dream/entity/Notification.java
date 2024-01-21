package com.backend.dream.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idaccount")
    private Account account;

    @Column(name = "title")
    private String notificationTitle;

    @Column(name = "notification_text")
    private String notificationText;

    @Column(name = "created_at")
    private Timestamp createdTime;

    @ManyToOne
    @JoinColumn(name = "idrole")
    private Role role;

    private String image;
}
