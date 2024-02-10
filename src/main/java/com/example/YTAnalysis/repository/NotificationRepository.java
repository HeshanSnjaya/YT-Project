package com.example.YTAnalysis.repository;

import com.example.YTAnalysis.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    // Define a method to check if a record with a specific videoId exists
    boolean existsByVideoId(String videoId);
}
