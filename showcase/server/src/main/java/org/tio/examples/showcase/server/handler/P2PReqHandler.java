package org.tio.examples.showcase.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Tio;
import org.tio.core.ChannelContext;
import org.tio.examples.showcase.common.ShowcasePacket;
import org.tio.examples.showcase.common.ShowcaseSessionContext;
import org.tio.examples.showcase.common.Type;
import org.tio.examples.showcase.common.intf.AbsShowcaseBsHandler;
import org.tio.examples.showcase.common.packets.P2PReqBody;
import org.tio.examples.showcase.common.packets.P2PRespBody;
import org.tio.utils.json.Json;

/**
 * @author tanyaowu
 * 2017年3月27日 下午9:51:28
 */
public class P2PReqHandler extends AbsShowcaseBsHandler<P2PReqBody> {
	private static Logger log = LoggerFactory.getLogger(P2PReqHandler.class);

	/**
	 * @param args
	 * @author tanyaowu
	 */
	public static void main(String[] args) {

	}

	/**
	 *
	 * @author tanyaowu
	 */
	public P2PReqHandler() {
	}

	/**
	 * @return
	 * @author tanyaowu
	 */
	@Override
	public Class<P2PReqBody> bodyClass() {
		return P2PReqBody.class;
	}

	/**
	 * @param packet
	 * @param bsBody
	 * @param channelContext
	 * @return
	 * @throws Exception
	 * @author tanyaowu
	 */
	@Override
	public Object handler(ShowcasePacket packet, P2PReqBody bsBody, ChannelContext channelContext) throws Exception {
		log.info("收到点对点请求消息:{}", Json.toJson(bsBody));

		ShowcaseSessionContext showcaseSessionContext = (ShowcaseSessionContext) channelContext.getAttribute();

		P2PRespBody p2pRespBody = new P2PRespBody();
		p2pRespBody.setFromUserid(showcaseSessionContext.getUserid());
		p2pRespBody.setText(bsBody.getText());

		ShowcasePacket respPacket = new ShowcasePacket();
		respPacket.setType(Type.P2P_RESP);
		respPacket.setBody(Json.toJson(p2pRespBody).getBytes(ShowcasePacket.CHARSET));
		Tio.sendToUser(channelContext.groupContext, bsBody.getToUserid(), respPacket);

		return null;
	}
}
