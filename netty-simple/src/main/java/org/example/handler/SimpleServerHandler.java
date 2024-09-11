package org.example.handler;

import com.alibaba.fastjson.JSONObject;
import com.song.netty.utill.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.example.utill.Request;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        Request request = JSONObject.parseObject(msg.toString(), Request.class);
        System.out.println("server received from client-------------------------");
        Response response = new Response();
        response.setId(request.getId());
        response.setContent("hello, it's server, I just received your request.");
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

