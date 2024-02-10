package com.example.YTAnalysis.controller;

import com.example.YTAnalysis.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/subscribe")
    public ResponseEntity<String>subscribeToPubSub(
            @RequestParam String channelId,
            @RequestParam String mode
    ){
        return subscribeService.subscribe(channelId,mode);
    }
}
