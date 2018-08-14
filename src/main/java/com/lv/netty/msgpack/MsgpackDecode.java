package com.lv.netty.msgpack;

import com.lv.netty.BaseResult;
import com.lv.netty.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @Author: lvrongzhuan
 * @Description:自定义Msgpack解码器
 * @Date: 2018/8/9 11:39
 * @Version: 1.0
* modified by:
 */
public class MsgpackDecode extends MessageToMessageDecoder<ByteBuf> {
   /* @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
   int length =  in.readableBytes();
        byte[] bytes = new byte[length];
        in.getBytes(length,bytes,0,length);
        MessagePack messagePack = new MessagePack();
        out.add(messagePack.read(bytes));
        in.clear();
    }*/

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int length =  msg.readableBytes();
        byte[] bytes = new byte[length];
        msg.getBytes(msg.readerIndex(),bytes,0,length);
        MessagePack messagePack = new MessagePack();
        out.add(messagePack.read(bytes,User.class));
    }

    /*@Override
    protected void decode(ChannelHandlerContext ctx, BaseResult msg, List<Object> out) throws Exception {

    }*/
}
