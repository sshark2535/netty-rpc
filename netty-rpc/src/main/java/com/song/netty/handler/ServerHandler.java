package com.song.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.song.netty.medium.Media;
import com.song.netty.utill.Request;
import com.song.netty.utill.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        Request serverRequest = JSONObject.parseObject(msg.toString(), Request.class);
        Media media = Media.newInstance(); // 中介者模式
        Response response = media.process(serverRequest);
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"\r\n");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state().equals(IdleState.READER_IDLE)){
                System.out.println("----------READER IDLE-------------");
                ctx.close();
            }
            else if(event.state().equals(IdleState.WRITER_IDLE)){
                System.out.println("----------WRITE IDLE-------------");
            }
            else if(event.state().equals(IdleState.ALL_IDLE)){
                System.out.println("----------ALL IDLE-------------");
                ctx.channel().writeAndFlush("ping\r\n");
            }
        }
    }
}

