/**
 *
 */
package org.tio.examples.im.common.bs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartshowRespBody extends BaseRespBody {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(StartshowRespBody.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/**
	 *
	 */
	private Integer liveshowid;

	/**
	 * rtmp推流地址
	 */
	private java.lang.String rtmppublishurl;

	/**
	 * rtmp播放地址
	 */
	private java.lang.String rtmpliveurl;

	/**
	 *
	 */
	public StartshowRespBody() {

	}

}
