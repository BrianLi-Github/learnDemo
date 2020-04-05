package com.example.demo.netty.utils;

import io.netty.buffer.ByteBuf;

public class ByteBufToBytes {
	/**
	 * 将ByteBuf转换为byte[]
	 * @param data
	 * @return
	 */
	public byte[] read(ByteBuf data) {
		byte[] bytes = new byte[data.readableBytes()];// 创建byte[]
		data.readBytes(bytes);// 将ByteBuf转换为byte[]
		return bytes;
	}
}
