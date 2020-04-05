package com.example.demo.netty.bean;

import com.example.demo.netty.utils.ByteBufToBytes;
import com.example.demo.netty.utils.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 反序列化
 * 将Byte[]转换为Object
 *
 */
public class MessageObjDecoder extends ByteToMessageDecoder {
   @Override
   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
       //工具类：将ByteBuf转换为byte[]
       ByteBufToBytes read = new ByteBufToBytes();
       byte[] bytes = read.read(in);
       //工具类：将byte[]转换为object
       Object obj = ByteObjConverter.byteToObject(bytes);
       if (obj != null) {
           out.add(obj);
       }
   }

}
