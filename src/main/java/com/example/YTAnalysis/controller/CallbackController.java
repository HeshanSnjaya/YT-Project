package com.example.YTAnalysis.controller;

import com.example.YTAnalysis.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CallbackController {
    private final NotificationService notificationService;

    @RequestMapping(value = "/callbackPoint", method = { RequestMethod.GET, RequestMethod.POST })
    ResponseEntity<?> notification(RequestEntity<String> payload,
                            @RequestParam(value = "hub.challenge", required = false) String hubChallenge) {
        if (StringUtils.hasText(hubChallenge)) {
            System.out.println("hub challenge " + hubChallenge);
            return ResponseEntity.ok(hubChallenge);
        }
        System.out.println("Notification received");
        System.out.println("payload :\n"+ payload.getBody());
        return notificationService.processAtomFeed(payload.getBody());
    }


}
