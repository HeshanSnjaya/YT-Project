package com.example.YTAnalysis.service;

import com.example.YTAnalysis.entity.Channel;
import com.example.YTAnalysis.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final ChannelRepository channelRepository;

    public ResponseEntity<String> subscribe(String channelId,String subMode) {
        String subscribeEndpoint = "https://pubsubhubbub.appspot.com/subscribe";
        String topic = "https://www.youtube.com/xml/feeds/videos.xml?channel_id=" + channelId;
        String callback = "https://ytbackend-jftb.onrender.com/api/v1/callbackPoint";
        String verify = "SYNC"; // or "ASYNC"
        int leaseSeconds = 3600*24*7; // specify the lease duration in seconds

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set up request body with query parameters
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("hub.topic", topic);
        requestBody.add("hub.callback", callback);
        requestBody.add("hub.verify", verify);
        requestBody.add("hub.mode", subMode);
        requestBody.add("hub.lease_seconds", String.valueOf(leaseSeconds));

        // Set up the request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Create a RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send the POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                subscribeEndpoint,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // If the subscribe operation is successful, set subscribeStatus as true
        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            Channel channel = channelRepository.findByChannelId(channelId);
            if (channel != null) {
                channel.setSubscribeStatus(true);
                channelRepository.save(channel);
            }
        }

        return responseEntity;
    }
    @Transactional
    public ResponseEntity<String> subscribeAllUnsubscribedChannels() {
        List<Channel> unsubscribedChannels = channelRepository.findBySubscribeStatusFalse();

        for (Channel channel : unsubscribedChannels) {
            subscribe(channel.getChannelId(), "subscribe");
        }

        return ResponseEntity.ok("Subscribed to all unsubscribed channels");
    }


}
