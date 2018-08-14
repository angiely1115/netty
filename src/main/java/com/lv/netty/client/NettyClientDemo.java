package com.lv.netty.client;

import com.lv.netty.handle.TimeClientHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Author: lvrongzhuan
 * @Description: netty客户端
 * @Date: 2018/8/1 17:24
 * @Version: 1.0
 * modified by:
 */
public class NettyClientDemo {
    public void connect(String host,int port) throws InterruptedException {
        //创建线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024))
                            .addLast(new StringDecoder()).addLast(new TimeClientHandle());//需要放在最后，注意顺序
                        }
                    });
           ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
           channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NettyClientDemo nettyClientDemo = new NettyClientDemo();
        nettyClientDemo.connect("127.0.0.1",9090);
    }
}
