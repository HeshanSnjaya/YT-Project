package com.example.YTAnalysis.repository;

import com.example.YTAnalysis.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel,Long> {

    List<Channel> findBySubscribeStatusFalseAndExistTrue();
    boolean existsByChannelIdAndExist(String channelId,Boolean exist);
    boolean existsByChannelId(String channelId);
    Channel findByChannelId(String channelId);

    // Delete channel by channelId
    void deleteByChannelId(String channelId);
}
