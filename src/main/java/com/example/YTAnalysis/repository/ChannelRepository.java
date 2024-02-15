package com.example.YTAnalysis.repository;

import com.example.YTAnalysis.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel,Long> {

    List<Channel> findBySubscribeStatusFalse();
    boolean existsByChannelId(String channelId);
}
