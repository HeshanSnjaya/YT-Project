package com.example.YTAnalysis.repository;

import com.example.YTAnalysis.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    boolean existsByVideoId(String videoId);
    List<Notification> findTop100ByReviewedFalseAndArchivedFalseOrderByNotificationIdDesc();
    List<Notification> findTop10ByReviewedFalseAndAssignedFalseAndArchivedFalseOrderByNotificationIdDesc();
    List<Notification> findByReviewedAndAssignedAndAssignedSlotAndArchivedFalseOrderByNotificationIdDesc(Boolean reviewed, Boolean assigned, Integer assignedSlot);

    // Find notifications where reviewed is false and archived is true
    List<Notification> findByReviewedFalseAndArchivedTrue();
}
