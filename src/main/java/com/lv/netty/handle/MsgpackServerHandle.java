package com.lv.netty.handle;

import com.lv.netty.BaseResult;
import com.lv.netty.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/9 12:01
 * @Version: 1.0
 * modified by:
 */
public class MsgpackServerHandle extends ChannelInboundHandlerAdapter{
  /*  @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端，客户端连接激活");
        System.out.println("ChannelConfig:->options:"+ctx.channel().config().getOptions());
        System.out.println("eventLoop:"+ctx.channel().eventLoop());
        System.out.println("pipeline:"+ctx.channel().pipeline());
        System.out.println("localAddress:"+ctx.channel().localAddress());
        System.out.println("remoteAddress:"+ctx.channel().remoteAddress());
        System.out.println("metadata:"+ctx.channel().metadata());
        System.out.println("name:"+ctx.name());
        super.channelActive(ctx);
    }*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        BaseResult<User> userBaseResult = (BaseResult<User>) msg;
        System.out.println("Object msg:"+msg.getClass());
        System.out.println("服务端接收消息："+msg);
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端读事件完成");
//        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
