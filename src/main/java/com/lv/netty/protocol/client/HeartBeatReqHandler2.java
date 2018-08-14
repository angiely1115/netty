package com.lv.netty.protocol.client;

import com.lv.netty.protocol.MessageType;
import com.lv.netty.protocol.struct.Header;
import com.lv.netty.protocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeatReqHandler2 extends ChannelHandlerAdapter {
    private  final Logger log= LoggerFactory.getLogger(HeartBeatReqHandler.class);
	
	//线程安全心跳失败计数器
	private AtomicInteger unRecPongTimes = new AtomicInteger(1);
	
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyMessage message = (NettyMessage)msg;
        //服务器端心跳回复
        if(message.getHeader() != null  && message.getHeader().getType() == MessageType.HEARTBEAT_RESP
                .value()){
        	//如果服务器进行pong心跳回复，则清零失败心跳计数器
        	unRecPongTimes = new AtomicInteger(1);
        	log.debug("client receive server pong msg :---->"+message);
        }else{
        	ctx.fireChannelRead(msg);
        }
    }  
    
    /**
     * 事件触发器，该处用来处理客户端空闲超时,发送心跳维持连接。
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {
                /*读超时*/  
                log.info("===客户端===(READER_IDLE 读超时)");
            } else if (event.state() == IdleState.WRITER_IDLE) {  
                /*客户端写超时*/     
            	log.info("===客户端===(WRITER_IDLE 写超时)");
                unRecPongTimes.getAndIncrement();  
                //服务端未进行pong心跳响应的次数小于3,则进行发送心跳，否则则断开连接。
                if(unRecPongTimes.intValue() < 3){  
                	//发送心跳，维持连接
                    ctx.channel().writeAndFlush(buildHeatBeat()) ;
                    log.info("客户端：发送心跳");
                }else{
                    System.out.println("客户端关闭");
                    ctx.channel().close();  
                }  
            } else if (event.state() == IdleState.ALL_IDLE) {  
                /*总超时*/  
            	log.info("===客户端===(ALL_IDLE 总超时)");  
            }  
        }  
    }
        
   /* private NettyMessageProto buildHeartBeat(){
    	HeaderProto header = HeaderProto.newBuilder().setType(Constants.MSGTYPE_HEARTBEAT_REQUEST).build();
    	NettyMessageProto  message = NettyMessageProto.newBuilder().setHeader(header).build();
		return message;
	}*/
    private NettyMessage buildHeatBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_REQ.value());
        message.setHeader(header);
        return message;
    }
    
    /**
     * 异常处理
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception{
    	ctx.fireExceptionCaught(cause);
    }
 
}