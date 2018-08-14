package com.lv.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * @Author: lvrongzhuan
 * @Description: netty 自己逻辑处理
 * @Date: 2018/8/1 16:29
 * @Version: 1.0
 * modified by:
 */
public class TimeServerHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String buf = (String) msg;
        /*int size = buf.readableBytes();
        System.out.println("size:"+size);
        byte[] bytes = new byte[size];
        buf.readBytes(bytes);*/
        System.out.println("***********"+msg.getClass());
        System.out.println("接收数据:"+buf);
        System.out.println("ChannelHandlerContext:name:"+ctx.name());
        ByteBuf resqbuf = Unpooled.copiedBuffer(buf.getBytes());
        ctx.write(resqbuf);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
