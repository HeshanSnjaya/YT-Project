package com.example.YTAnalysis.service;

import com.example.YTAnalysis.dto.NotificationDTO;
import com.example.YTAnalysis.entity.Notification;
import com.example.YTAnalysis.repository.NotificationRepository;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public ResponseEntity<String> processAtomFeed(String atomFeedXml) {
        try {

            String videoId=parseElement(atomFeedXml,"yt:videoId");
            String channelId=parseElement(atomFeedXml,"yt:channelId");
            String videoTitle=parseElement(atomFeedXml,"title");
            String publishedDate=parseElement(atomFeedXml,"published");
            String updatedDate=parseElement(atomFeedXml,"updated");
            System.out.println("VideoId :"+videoId);

            // Check if video ID already exists in the database
            if (notificationRepository.existsByVideoId(videoId)){
                // Video ID already exists, do not add it again
                System.out.println("Video Id is already in database and not a new video");
                return ResponseEntity.status(204).build();
            }
            // Save to the database
            Notification notification = new Notification();
            notification.setVideoId(videoId);
            notification.setChannelId(channelId);
            notification.setVideoTitle(videoTitle);
            notification.setPublishedDateTime(publishedDate);
            notification.setUpdatedDateTime(updatedDate);
            notification.setTrackedTime(OffsetDateTime.now());
            notificationRepository.save(notification);
            // Convert to DTO
            NotificationDTO notificationDto = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDto);
            // Respond to the sender
            System.out.println("YouTube video feed notification received and saved successfully");
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            // Handle parsing or processing errors
            e.printStackTrace();
            //This need to be handled otherwise some notification may be missed
            System.out.println("Notification missed");
            return ResponseEntity.status(204).build();
        }
    }

    private static String parseElement(String atomFeedXml, String elementName) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(atomFeedXml)));

            NodeList nodes = document.getElementsByTagName(elementName);
            if (nodes.getLength() > 0) {
                return nodes.item(0).getTextContent();
            } else {
                throw new IllegalArgumentException(elementName + " element not found in Atom feed XML");
            }
        } catch (ParserConfigurationException | IOException e) {
            throw new RuntimeException("Error parsing Atom feed XML", e);
        }
    }
}

