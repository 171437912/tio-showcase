package org.tio.examples.im.common;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tanyaowu
 * 2017年5月9日 上午11:21:54
 */
public class ByteBufferTest {
	private static Logger log = LoggerFactory.getLogger(ByteBufferTest.class);

	/**
	 * @param args
	 * @author tanyaowu
	 */
	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(6);
		byteBuffer.put((byte) 3);

		byteBuffer.position(0); //设置position到0位置，这样读数据时就从这个位置开始读
		byteBuffer.limit(1); //设置limit为1，表示当前bytebuffer的有效数据长度是1

		byte bs = byteBuffer.get();
		System.out.println(byteBuffer);
	}

	/**
	 *
	 * @author tanyaowu
	 */
	public ByteBufferTest() {
	}
}
