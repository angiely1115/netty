package com.lv.netty.client;

import com.lv.netty.handle.EchoClientHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/2 15:11
 * @Version: 1.0
 * modified by:
 */
public class  EchoClient {
    public void connect(String host,int port){
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ByteBuf byteBuf = Unpooled.copiedBuffer("$_".getBytes());
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf))
                                .addLast(new StringDecoder())
                                .addLast(new EchoClientHandle());

                    }
                });
        try {
           ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            System.out.println("客户端连接");
            Channel channel = channelFuture.channel();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String in = br.readLine();
            while (!(in.equals("再见") || in.equals("bye"))) {
                System.out.println("br.readLine:"+in);
                ByteBuf byteBuf = Unpooled.copiedBuffer((in+" $_").getBytes());
                channel.writeAndFlush(byteBuf);
                in = br.readLine();
            }
            ByteBuf byteBuf = Unpooled.copiedBuffer((in+" $_").getBytes());
            channel.writeAndFlush(byteBuf);
            channel.closeFuture();
            channel.close();
            System.out.println("客户端关闭");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clientGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoClient().connect("127.0.0.1",9999);

    }
}
