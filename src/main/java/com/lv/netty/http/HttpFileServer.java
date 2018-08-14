package com.lv.netty.http;

import com.lv.netty.handle.HttpFileServerHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author: lvrongzhuan
 * @Description:用netty简单实现一个http的文件服务器
 * @Date: 2018/8/3 11:15
 * @Version: 1.0
 * modified by:
 */
public class HttpFileServer {

    public void start(int port){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup,workGroup).option(ChannelOption.SO_BACKLOG,100)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder())
                                .addLast("http-aggregator",new HttpObjectAggregator(65536))
                                .addLast("http-encoder",new HttpResponseEncoder())
                                .addLast("http-chunked",new ChunkedWriteHandler())
                                .addLast(new HttpFileServerHandle());
                    }
                });


           ChannelFuture channelFuture =  bootstrap.bind(port).sync();
           channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpFileServer().start(8080);
    }
}
