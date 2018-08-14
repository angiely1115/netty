package com.lv.netty.handle;

import com.lv.netty.BaseResult;
import com.lv.netty.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/9 12:43
 * @Version: 1.0
 * modified by:
 */
public class MsgpackClientHandle extends ChannelInboundHandlerAdapter{
   /* @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端注册事件");
        System.out.println("ChannelConfig:->options:"+ctx.channel().config().getOptions());
        System.out.println("eventLoop:"+ctx.channel().eventLoop());
        System.out.println("pipeline:"+ctx.channel().pipeline());
        System.out.println("localAddress:"+ctx.channel().localAddress());
        System.out.println("remoteAddress:"+ctx.channel().remoteAddress());
        System.out.println("metadata:"+ctx.channel().metadata());
        System.out.println("name:"+ctx.name());
        for (int i=0;i<10;i++){
            User user = new User("ABCDEFG --->" + i,i);
            ctx.writeAndFlush(BaseResult.success(user));
        }
        super.channelRegistered(ctx);
    }*/

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i=0;i<10;i++){
            User user = new User("ABCDEFG --->" + i,i,new User[]{new User()});
            ctx.writeAndFlush(user);
//            ctx.flush();
//            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("客户端已激活");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端接收数据:"+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        System.out.println("客户端 读准备完成");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
