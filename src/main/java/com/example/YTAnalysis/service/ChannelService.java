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
            channels.add(new Channel(null, channelId, false));
        }
        channelRepository.saveAll(channels);
    }
}
