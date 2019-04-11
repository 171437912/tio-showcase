package org.tio.examples.im.client.handler;

import org.tio.core.ChannelContext;
import org.tio.examples.im.common.ImPacket;

/**
 *
 * @author tanyaowu
 *
 */
public interface ImAioHandlerIntf {
	/**
	 *
	 * @param packet
	 * @param channelContext
	 * @return
	 * @throws Exception
	 *
	 * @author tanyaowu
	 * 2017年2月22日 下午2:02:30
	 *
	 */
	public Object handler(ImPacket packet, ChannelContext channelContext) throws Exception;
}
