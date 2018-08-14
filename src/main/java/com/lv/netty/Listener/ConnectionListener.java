package com.lv.netty.Listener;

import com.lv.netty.client.MsgpackClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * @Author: lvrongzhuan
 * @Description: 重试连接监听
 * @Date: 2018/8/9 16:21
 * @Version: 1.0
 * modified by:
 */
public class ConnectionListener implements ChannelFutureListener{
    private MsgpackClient client;

    public ConnectionListener(MsgpackClient client) {
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()) {
            System.out.println("Reconnection");
            final EventLoop eventLoop = future.channel().eventLoop();
            eventLoop.schedule(new Runnable() {
                @Override
                public void run() {
                    client.connect("127.0.0.1",9999);
                }
            }, 1L, TimeUnit.SECONDS);
        }

    }
}
