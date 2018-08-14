package com.lv.netty.server;

import com.lv.netty.handle.MsgpackServerHandle;
import com.lv.netty.msgpack.MsgpackDecode;
import com.lv.netty.msgpack.MsgpackEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author: lvrongzhuan
 * @Description:以Msgpack作为码流编解码服务
 * @Date: 2018/8/2 14:20
 * @Version: 1.0
 * modified by:
 */
public class MsgpackServer {
    public void start(int port){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
        bootstrap.group(boosGroup,workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                .addLast("msgpack decoder",new MsgpackDecode())
                                .addLast("frameEncoder", new LengthFieldPrepender(2))
                                .addLast("msgpack encoder",new MsgpackEncode())
                                .addLast(new MsgpackServerHandle());
                    }
                });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
            System.out.println("服务端关闭。。。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MsgpackServer().start(9999);
    }
}
