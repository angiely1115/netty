package com.lv.netty.protocol.server;

import com.lv.netty.protocol.MessageType;
import com.lv.netty.protocol.struct.Header;
import com.lv.netty.protocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务端心跳检测第二版
 */
public class HeartBeatRespHandler2 extends ChannelInboundHandlerAdapter {
	private final Logger log = LoggerFactory.getLogger(HeartBeatRespHandler2.class);
	//线程安全心跳失败计数器
	private AtomicInteger unRecPingTimes = new AtomicInteger(1);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//清零心跳失败计数器 重置
		unRecPingTimes = new AtomicInteger(1);
		//接收客户端心跳信息
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ
				.value()) {
			log.info("Receive client heart beat message : ---> "
					+ message);
			//log.info("server receive client" + ctx.channel().attr(SysConst.SERIALNO_KEY) + " ping msg :---->" + message);
			//接收客户端心跳后,进行心跳响应
			NettyMessage replyMsg = buildHeartBeat();
			ctx.writeAndFlush(replyMsg);
		} else {
			ctx.fireChannelRead(msg);
		}
	}


	/**
	 * 事件触发器，该处用来处理客户端空闲超时,发送心跳维持连接。
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("事件触发器******");
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
	                /*读超时*/
				log.info("===服务器端===(READER_IDLE 读超时)");
				unRecPingTimes.getAndIncrement();
				//客户端未进行ping心跳发送的次数等于3,断开此连接
				if (unRecPingTimes.intValue() == 3) {
					ctx.disconnect();
					System.out.println("此客户端连接超时，服务器主动关闭此连接....");
					log.info("此客户端连接超时，服务器主动关闭此连接....");
				}
			} else if (event.state() == IdleState.WRITER_IDLE) {
	                /*服务端写超时*/
				log.info("===服务器端===(WRITER_IDLE 写超时)");

			} else if (event.state() == IdleState.ALL_IDLE) {
	                /*总超时*/
				log.info("===服务器端===(ALL_IDLE 总超时)");
			}
		}
	}


	/**
	 * 创建心跳响应消息
	 *
	 * @return
	 */
	private NettyMessage buildHeartBeat() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
	/*	   Header header = HeaderProto.newBuilder().setType(Constants.MSGTYPE_HEARTBEAT_RESPONSE).build();
		   NettyMessage message =NettyMessage.newBuilder().setHeader(header).build();*/
		return message;
	}
}