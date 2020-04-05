package com.example.demo.netty.server;

import com.example.demo.netty.bean.MessageObj;
import com.example.demo.netty.bean.ResponseModal;
import com.example.demo.netty.utils.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务器数据读取处理
 */
public class ServerHandler1 extends ChannelInboundHandlerAdapter {

    /**
     * channel 通道读取到数据时被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        MessageObj msgObj = (MessageObj) msg;
        System.out.println(msgObj.getObjId());
        System.out.println(msgObj.getMsg());
        System.out.println(msgObj.getActionTime());

        ResponseModal responseModal = new ResponseModal();
        responseModal.setStatus("0");
        responseModal.setMessage("当前是服务器返回的信息.");
        ByteBuf resp = Unpooled.copiedBuffer(ByteObjConverter.objectToByte(responseModal));
        ctx.write(resp);
    }


    /**
     * channel 通道数据读取完后被调用 --> 即channelRead 后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server 读取数据完毕..");
        ctx.flush();//刷新后才将数据发出到SocketChannel
    }

    /**
     * 处理异常时被调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
