package com.song.netty.init;

import com.song.netty.constant.Constants;
import com.song.netty.factory.ZookeeperFactory;
import com.song.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class NettyInitial implements ApplicationListener<ContextRefreshedEvent> {
    public void run() throws Exception {
        int port = 8080;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 分隔符解码器DelimiterBasedFrameDecoder，防止TCP粘包分包的问题
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                            // 心跳机制
                            //ch.pipeline().addLast(new IdleStateHandler(60,45,20, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ServerHandler());
                            ch.pipeline().addLast(new StringEncoder()); // 二进制流转string
                        }
                    });
            ChannelFuture f = b.bind(port).sync(); // 建立连接

            // 连zookeeper
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress serverIpAddress = InetAddress.getLocalHost();
            //临时目录节点, 一旦创建这个节点当会话结束, 这个节点会被自动删除
            client.create().withMode(CreateMode.EPHEMERAL).forPath(Constants.SERVER_PATH + serverIpAddress.getHostAddress()+"#"+port+"#");

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            this.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
