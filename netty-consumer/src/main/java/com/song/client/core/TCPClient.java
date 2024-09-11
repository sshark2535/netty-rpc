package com.song.client.core;

import com.alibaba.fastjson.JSONObject;
import com.song.client.constant.Constants;
import com.song.client.param.ClientRequest;
import com.song.client.param.Response;
import com.song.netty.factory.ZookeeperFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import com.song.client.handler.SimpleClientHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.server.ServerWatcher;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TCPClient {
    static final Bootstrap b = new Bootstrap(); // (1)
    static ChannelFuture f = null;
    static Set<String> pathSet = new HashSet<String >();
    static {
        String host = "localhost";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new SimpleClientHandler());
                ch.pipeline().addLast(new StringEncoder());
            }
        });

        // 从zookeeper列表里获取server信息
        CuratorFramework client = ZookeeperFactory.create();
        try {
            List<String> pathList = client.getChildren().forPath(Constants.SERVER_PATH);

            // 添加实时监听
            CuratorWatcher watcher = new ClientServerWatcher();
            client.getChildren().usingWatcher(watcher).forPath(Constants.SERVER_PATH);

            for(String path:pathList){
                String[] str = path.split("#");
                String h = str[0];
                Integer p = Integer.valueOf(str[1]);
                pathSet.add(h+"#"+p);
                ChannelFuture channelFuture = TCPClient.b.connect(h,p);
                ChannelManager.add(channelFuture);
            }

//            if(pathSet.size()>0){
//                String[] str = pathSet.toArray().toString().split("#");
//                host = str[0];
//                port = Integer.valueOf(str[1]);
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Start the client.
//        try {
//            f = b.connect(host, port).sync(); // (5)
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    static AtomicInteger i = new AtomicInteger(1);
    public static Response send(ClientRequest request) {
        f = ChannelManager.get(i);
        f.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
        DefaultFuture df = new DefaultFuture(request);
        return df.get();
    }

}
