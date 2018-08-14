package com.lv.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/2 15:28
 * @Version: 1.0
 * modified by:
 */
public class EchoClientHandle extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接激活");
        String echoReq = "Welcome to Netty hello Netty $_";
        ByteBuf byteBuf = null;
   /*     for (int i=0;i<50;i++){
            byteBuf = Unpooled.copiedBuffer((i+echoReq).getBytes());
            ctx.writeAndFlush(byteBuf);
        }*/
        System.out.println("ChannelConfig:->options:"+ctx.channel().config().getOptions());
        System.out.println("eventLoop:"+ctx.channel().eventLoop());
        System.out.println("pipeline:"+ctx.channel().pipeline());
        System.out.println("localAddress:"+ctx.channel().localAddress());
        System.out.println("remoteAddress:"+ctx.channel().remoteAddress());
        System.out.println("metadata:"+ctx.channel().metadata());
        System.out.println("name:"+ctx.name());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String msgStr = (String)msg;
        System.out.println("接收服务器的消息："+msgStr);
//        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        System.out.println("数据接收完毕》》》》》");
/*
        while (true){
            TimeUnit.SECONDS.sleep(1);
            System.out.println("读完数据是否还保持连接："+ctx.channel().isActive());
        }
*/
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println(ctx.channel().localAddress()+"客户端连接断开");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress()+"加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress()+"离开");
    }
}
