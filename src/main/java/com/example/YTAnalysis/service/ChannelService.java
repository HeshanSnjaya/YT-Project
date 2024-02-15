package com.example.YTAnalysis.service;

import com.example.YTAnalysis.entity.Channel;
import com.example.YTAnalysis.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    @Transactional
    public void saveChannels(List<String> channelIds) {
        List<Channel> channels = new ArrayList<>();
        for (String channelId : channelIds) {
            // Check if the channelId already exists in the database
            if (!channelRepository.existsByChannelId(channelId)) {
                channels.add(new Channel(null, channelId, false));
            }
        }
        // Save only the channels that don't already exist
        if (!channels.isEmpty()) {
            channelRepository.saveAll(channels);
        }
    }
}
