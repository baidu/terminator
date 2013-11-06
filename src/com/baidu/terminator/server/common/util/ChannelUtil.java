/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common.util;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

public class ChannelUtil {

	/**
	 * Closes the specified channel after all queued write requests are flushed.
	 * 
	 * @param ch
	 *            the specified channel
	 */
	public static void closeOnFlush(Channel ch) {
		if (ch.isConnected()) {
			ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(
					ChannelFutureListener.CLOSE);
		}
	}

	public static byte[] readChannelBufferAsByte(ChannelBuffer buffer) {
		byte[] bytes = new byte[buffer.readableBytes()];

		int i = 0;
		while (buffer.readable()) {
			bytes[i] = buffer.readByte();
			i++;
		}
		return bytes;
	}

	public static String readChannelBufferAsString(ChannelBuffer buffer) {
		byte[] bytes = readChannelBufferAsByte(buffer);
		return new String(bytes);
	}

}
