package com.example.YTAnalysis.repository;

import com.example.YTAnalysis.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    boolean existsByVideoId(String videoId);
}
