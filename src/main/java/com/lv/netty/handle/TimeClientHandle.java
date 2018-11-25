package com.lv.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/1 17:42
 * @Version: 1.0
 * modified by:
 */
public class TimeClientHandle extends ChannelInboundHandlerAdapter{
    String strData = "chuangjian菲菲飞鸟"+System.getProperty("line.separator");
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与服务端建立连接");
        for(int i=0;i<100;i++){
            ByteBuf byteBuf = Unpooled.buffer(strData.length());
            byteBuf.writeBytes(strData.getBytes());
            ctx.writeAndFlush(byteBuf);
        }

//        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取服务端返回的数据
        String byteBuf = (String) msg;
       /* int size = byteBuf.readableBytes();
        byte[] bytes = new byte[size];
        byteBuf.readBytes(bytes);*/
        System.out.println("返回的数据:"+byteBuf);
//        super.channelRead(ctx, msg);
        ctx.close().addListener(ChannelFutureListener.CLOSE);// 关闭客户端
        ReferenceCountUtil.release(msg);//释放
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
