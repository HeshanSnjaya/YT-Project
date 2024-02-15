package com.example.YTAnalysis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    private String videoId;
    private String channelId;
    private String videoTitle;
    private String publishedDateTime;
    private String updatedDateTime;
    private OffsetDateTime trackedTime;
    private Boolean claimable;
    private Boolean reviewed;
}
