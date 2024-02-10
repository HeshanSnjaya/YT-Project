package com.example.YTAnalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationDTO {
    private Long notificationId;
    private String videoId;
    private String channelId;
    private String videoTitle;

}
