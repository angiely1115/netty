package com.lv.netty.msgpack;

import com.lv.netty.BaseResult;
import com.lv.netty.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @Author: lvrongzhuan
 * @Description: 自定义msgpack编码器
 * @Date: 2018/8/9 11:38
 * @Version: 1.0
 * modified by:
 */
public class MsgpackEncode extends MessageToByteEncoder<User>{
    @Override
    protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();
        byte[] bytes = messagePack.write(msg);
        out.writeBytes(bytes);
    }

 /*   @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();
        byte[] bytes = messagePack.write(msg);
        out.writeBytes(bytes);
    }*/
}
