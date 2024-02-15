package com.example.YTAnalysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

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

        return responseEntity;
    }

}
