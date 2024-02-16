package com.example.YTAnalysis.controller;

import com.example.YTAnalysis.service.ChannelService;
import com.example.YTAnalysis.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class SubscribeController {

    private final SubscribeService subscribeService;
    private final ChannelService channelService;


    @PostMapping(value ="/subscribe")
    public ResponseEntity<?>subscribeToPubSub(
            @RequestParam String channelId){
        // Create an empty list to store channel IDs
        List<String> channelList = new ArrayList<>();
        channelList.add(channelId);
        channelService.saveChannels(channelList);
        return subscribeService.subscribeAllUnsubscribedChannels();
    }

    @PostMapping(value ="/subscribeAllUnsubscribed")
    public ResponseEntity<String> subscribeAllUnsubscribed() {
        return subscribeService.subscribeAllUnsubscribedChannels();
    }



}
