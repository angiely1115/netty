package com.lv.netty.handle;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/3 11:38
 * @Version: 1.0
 * modified by:
 */
public class HttpFileServerHandle extends SimpleChannelInboundHandler<FullHttpRequest>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String uri = msg.uri();
        System.out.println("method:"+msg.method().name());
        System.out.println("请求的的uri:"+uri);
        List<String> strings = this.getParams(uri).get("name");
        String name = strings!=null?strings.get(0):"空空如也";
        System.out.println(name);
        if(StringUtil.length(name)==0){
            name = "请输入名称";
        }
        String resp = "<html><head><meta charset=utf-8></head><body>name:"+name+"</body></html>";
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(resp, CharsetUtil.UTF_8));
        response.headers().set("content_type", "text/html; charset=UTF-8");
        if (HttpUtil.isKeepAlive(msg)) {
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);;
    }
    private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: "
                + status + "\r\n", CharsetUtil.UTF_8));
        response.headers().set("Content-Type", "text/html; charset=UTF-8");
        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 获取请求参数
     * @param uri
     */
    public Map<String,List<String>> getParams(String uri){
        QueryStringDecoder stringDecoder = new QueryStringDecoder(uri);
        Map<String,List<String>> listMaps = stringDecoder.parameters();
        return listMaps;
    }
}
