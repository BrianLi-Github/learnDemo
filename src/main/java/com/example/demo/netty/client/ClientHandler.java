package com.example.demo.netty.client;

import com.example.demo.netty.bean.MessageObj;
import com.example.demo.netty.bean.ResponseModal;
import com.example.demo.netty.utils.ByteBufToBytes;
import com.example.demo.netty.utils.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 客户端连接服务器后被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageObj obj = new MessageObj();
        obj.setObjId(String.valueOf(System.currentTimeMillis()));
        obj.setMsg("Socket通道与服务器连接成功，开始构造数据...");
        obj.setActionTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        ctx.write(obj);
        ctx.flush();
    }

    /**
     * 从服务器接收到数据后调用该方法
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client 读取server返回的数据..");
        ResponseModal response = (ResponseModal)ByteObjConverter.byteToObject(new ByteBufToBytes().read(byteBuf));
        System.out.println(response);
    }


    /**
     * 发生异常时被调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常: " + cause.getMessage());
        // 释放资源
        ctx.close();
    }
}
