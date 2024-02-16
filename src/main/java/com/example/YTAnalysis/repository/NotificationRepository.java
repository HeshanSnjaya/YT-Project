package com.example.YTAnalysis.repository;

import com.example.YTAnalysis.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    boolean existsByVideoId(String videoId);
    List<Notification> findByReviewedFalse();

    List<Notification> findTop10ByReviewedAndAssignedOrderByNotificationIdAsc(Boolean reviewed, Boolean assigned);
    List<Notification> findByReviewedAndAssignedAndAssignedSlot(Boolean reviewed, Boolean assigned, Integer assignedSlot);
}
