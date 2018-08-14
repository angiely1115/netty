/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lv.netty.protocol.server;

import com.lv.netty.protocol.MessageType;
import com.lv.netty.protocol.struct.Header;
import com.lv.netty.protocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lilinfeng
 * @date 2014年3月15日
 * @version 1.0
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	private static final Log LOG = LogFactory.getLog(HeartBeatRespHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	NettyMessage message = (NettyMessage) msg;
	// 返回心跳应答消息
	if (message.getHeader() != null
		&& message.getHeader().getType() == MessageType.HEARTBEAT_REQ
			.value()) {
	    LOG.info("Receive client heart beat message : ---> "
		    + message);
	    NettyMessage heartBeat = buildHeatBeat();
	    LOG.info("Send heart beat response message to client : ---> "
			    + heartBeat);
//		TimeUnit.SECONDS.sleep(30);
	    ctx.writeAndFlush(heartBeat);
	} else
	    ctx.fireChannelRead(msg);
    }

    private NettyMessage buildHeatBeat() {
	NettyMessage message = new NettyMessage();
	Header header = new Header();
	header.setType(MessageType.HEARTBEAT_RESP.value());
	message.setHeader(header);
	return message;
    }

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("读完成>>>>>>>>>");
		super.channelReadComplete(ctx);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("增加一个handler>>>");
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("移除一个handler>>>>");
		super.handlerRemoved(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("通道注册》》》》");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("通道激活>>>>>");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("通道关闭无效>>>>>");
		super.channelInactive(ctx);
	}
}
