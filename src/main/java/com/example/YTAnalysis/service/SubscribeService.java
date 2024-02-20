package com.example.YTAnalysis.service;

import com.example.YTAnalysis.entity.Channel;
import com.example.YTAnalysis.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    public ResponseEntity<String> subscribe(String channelId,String subMode) {

                String subscribeEndpoint = "https://pubsubhubbub.appspot.com/subscribe";
                String topic = "https://www.youtube.com/xml/feeds/videos.xml?channel_id=" + channelId;
                String callback = "https://ytbackend-jftb.onrender.com/api/v1/callbackPoint";
                String verify = "SYNC"; // or "ASYNC"
                int leaseSeconds = 3600 * 24 * 7; // specify the lease duration in seconds

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

    public ResponseEntity<String> subscribeAllUnsubscribedChannels() {
        List<Channel> unsubscribedChannels = channelRepository.findBySubscribeStatusFalseAndExistTrue();

        for (Channel channel : unsubscribedChannels) {
            try{
                subscribe(channel.getChannelId(), "subscribe");
                channel.setSubscribeStatus(true);
                System.out.println("Channel subscription successful: "+channel.getChannelId());
            }
            catch(Exception e){
                channel.setSubscribeStatus(false);
                System.out.println("Channel subscription failed: "+channel.getChannelId());
                e.printStackTrace();
            }
            channelRepository.save(channel);

        }

        return ResponseEntity.ok("Subscribed to all possible unsubscribed channels");
    }

    public ResponseEntity<String> subscribeUnsubscribeChannel(String channelId,String subMode){
        if(subMode.equals("subscribe")){
            if (!channelRepository.existsByChannelId(channelId)){
                List<String> channelList = new ArrayList<>();
                channelList.add(channelId);
                channelService.saveChannels(channelList);
                subscribe(channelId,subMode);
                Channel channel=channelRepository.findByChannelId(channelId);
                channel.setSubscribeStatus(true);
                channel.setExist(true);
                channelRepository.save(channel);
                return ResponseEntity.ok("channel is successfully subscribed");
            }
            else if(channelRepository.existsByChannelIdAndExist(channelId,false)) {
                Channel channel=channelRepository.findByChannelId(channelId);
                subscribe(channelId,subMode);
                channel.setSubscribeStatus(true);
                channel.setExist(true);
                channelRepository.save(channel);
                return ResponseEntity.ok("channel is successfully subscribed");
            }
            else{
                return ResponseEntity.badRequest().body("Channel is already subscribed");
            }
        } else if (subMode.equals("unsubscribe")) {
            if (channelRepository.existsByChannelIdAndExist(channelId,true)){
                System.out.println("going to unsubscribe");
                subscribe(channelId,subMode);
                System.out.println("unsubscribed");
                Channel channel=channelRepository.findByChannelId(channelId);
                channel.setSubscribeStatus(false);
                channel.setExist(false);
                channelRepository.save(channel);
                System.out.println("channel is removed");
                return ResponseEntity.ok("channel is unsubscribed and removed");
            }
            else {
                return ResponseEntity.badRequest().body("Channel is not in the list");
            }
        }else{
            return ResponseEntity.badRequest().body("Invalid Sub Mode");
        }

    }


}
