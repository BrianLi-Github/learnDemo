package com.example.demo.netty.bean;

import com.example.demo.netty.utils.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 序列化
 * 将object转换成Byte[]
 *
 */
public class MessageObjEncoder extends MessageToByteEncoder<MessageObj> {

   @Override
   protected void encode(ChannelHandlerContext ctx, MessageObj msg, ByteBuf out) throws Exception {
       //工具类：将object转换为byte[]
       byte[] data = ByteObjConverter.objectToByte(msg);
       out.writeBytes(data);
       ctx.flush();
   }
}
