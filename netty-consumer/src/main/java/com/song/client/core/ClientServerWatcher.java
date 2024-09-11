package com.song.client.core;

import com.song.netty.factory.ZookeeperFactory;
import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.List;

public class ClientServerWatcher implements CuratorWatcher {
    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        CuratorFramework client = ZookeeperFactory.create();
        String path = watchedEvent.getPath();
        // 由于一个watcher注册一次只会监听一次，需要再次注册watcher到CuratorFramework上
        client.getChildren().usingWatcher(this);
        List<String> pathList = client.getChildren().forPath(path);
        //TCPClient.pathSet.clear();
        ChannelManager.pathSet.clear();
        for(String serverPath:pathList){ // 先遍历一遍添加到set里去重
            String[] str = serverPath.split("#");
            String host = str[0];
            Integer port = Integer.valueOf(str[1]);
            ChannelManager.pathSet.add(host+"#"+port);
        }
        ChannelManager.clear();
        for(String realPath: ChannelManager.pathSet){ // 加入channelManager
            String[] str = realPath.split("#");
            String host = str[0];
            Integer port = Integer.valueOf(str[1]);
            ChannelFuture channelFuture = TCPClient.b.connect(host, port);
            ChannelManager.add(channelFuture);
        }
    }
}
