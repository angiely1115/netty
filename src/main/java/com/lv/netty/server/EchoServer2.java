package com.lv.netty.server;

import com.lv.netty.handle.EchoServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/2 15:40
 * @Version: 1.0
 * modified by:
 */
public class EchoServer2 {
    public void start(int port){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(boosGroup,workerGroup).channel(NioServerSocketChannel.class)
                    //option表示设置一些TCP协议的相关参数 option设置ServerSocketChannel childOption设置socketChannel
                    .option(ChannelOption.SO_BACKLOG,100)
                    //设置boss线程处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //设置work线程组处理器，就是我们的业务处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ByteBuf byteBuf = Unpooled.copiedBuffer("$_".getBytes());
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(10))
                                    .addLast(new StringDecoder())
                                    .addLast(new EchoServerHandle());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoServer2().start(6666);
    }
}
