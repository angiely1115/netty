package com.lv.netty.server;

import com.lv.netty.handle.TimeServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/7/27 20:48
 * @Version: 1.0
 * modified by:
 */
public class NettyServerDemo {
    public void bind(int port) throws InterruptedException {
        //配置服务端NIO线程组
        //boos线程组
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        //工作线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChildChannelHandler());
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();//阻塞
        }finally {
            //优雅退出，释放线程池
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
    public static void main(String[] args) throws InterruptedException {
        NettyServerDemo nettyServerDemo = new NettyServerDemo();
        nettyServerDemo.bind(9090);
    }

    class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //添加自己的逻辑处理
            ch.pipeline().addLast(new StringDecoder()).addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new TimeServerHandle());
        }
    }
}
