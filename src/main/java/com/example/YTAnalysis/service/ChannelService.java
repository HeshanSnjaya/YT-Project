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
            if(!channelRepository.existsByChannelId(channelId)){
                channels.add(new Channel(null, channelId, false,true));
            } else if (channelRepository.existsByChannelIdAndExist(channelId,false)) {
                Channel channel=channelRepository.findByChannelId(channelId);
                channel.setExist(true);
                channelRepository.save(channel);
            }
        }
        // Save only the channels that don't already exist
        if (!channels.isEmpty()) {
            channelRepository.saveAll(channels);
        }
    }
}
