package com.example.YTAnalysis.controller;

import com.example.YTAnalysis.entity.Notification;
import com.example.YTAnalysis.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/notification")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping(value="/notification/archived")
    public ResponseEntity<List<Notification>> getUnreviewedAndArchivedNotifications() {
        List<Notification> notifications = notificationService.getUnreviewedAndArchivedNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PutMapping(value ="/notification/{notificationId}")
    public Notification updateNotification(@PathVariable Long notificationId,
                                           @RequestParam(required = false) Boolean claimable) {
        return notificationService.updateNotification(notificationId, claimable);
    }

    @PutMapping(value ="/notification/request/{slotId}")
    public ResponseEntity<List<Notification>> addTop10UnreviewedAndUnassignedNotifications(
            @PathVariable Integer slotId) {
        List<Notification> notifications = notificationService.addTop10UnreviewedAndUnassignedNotifications(slotId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping(value="/notification/archive/{notificationId}")
    public ResponseEntity<String> archiveNotification(@PathVariable Long notificationId) {
        return notificationService.archiveNotification(notificationId);
    }

    @PutMapping(value ="/notification/update-by-slot")
    public ResponseEntity<String> updateNotificationsBySlotId(@RequestParam Integer slotId) {
        notificationService.updateNotificationsBySlotId(slotId);
        return ResponseEntity.ok("Notifications updated successfully.");
    }

    @GetMapping(value ="/notification/by-slot/{slotId}")
    public ResponseEntity<List<Notification>> getNotificationsBySlotId(@PathVariable Integer slotId) {
        List<Notification> notifications = notificationService.getNotificationsBySlotId(slotId);
        return ResponseEntity.ok(notifications);
    }



}
