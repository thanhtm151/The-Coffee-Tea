package com.backend.dream.repository;

import com.backend.dream.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.account.id = :idAccount and n.role.id = 3 ORDER BY n.createdTime DESC")
    List<Notification> findAllNotifications(@Param("idAccount") Long idAccount);

    @Query("SELECT n FROM Notification n WHERE n.role.id = 1 OR n.role.id = 2 ORDER BY n.createdTime DESC")
    List<Notification> findAdminNotifications();
}
