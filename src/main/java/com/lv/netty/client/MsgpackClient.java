package com.lv.netty.client;

import com.lv.netty.Listener.ConnectionListener;
import com.lv.netty.handle.EchoClientHandle;
import com.lv.netty.handle.MsgpackClientHandle;
import com.lv.netty.msgpack.MsgpackDecode;
import com.lv.netty.msgpack.MsgpackEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/2 15:11
 * @Version: 1.0
 * modified by:
 */
public class MsgpackClient {
    public void connect(String host, int port) {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(clientGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                    .addLast("msgpack decoder", new MsgpackDecode())
                                    .addLast("frameEncoder", new LengthFieldPrepender(2))
                                    .addLast("msgpack encoder", new MsgpackEncode())
                                    .addLast(new MsgpackClientHandle());


                        }
                    });

//            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            bootstrap.remoteAddress(host, port);
            bootstrap.connect().addListener(new ConnectionListener(this));
            System.out.println("客户端连接");
//            channelFuture.addListener(new ConnectionListener(this));
//            channelFuture.channel().closeFuture().sync();
            System.out.println("客户端关闭");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MsgpackClient().connect("127.0.0.1", 9999);
    }
}
