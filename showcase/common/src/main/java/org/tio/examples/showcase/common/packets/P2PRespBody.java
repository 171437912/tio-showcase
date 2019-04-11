package org.tio.examples.showcase.common.packets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 点对点消息响应
 * @author tanyaowu
 * 2017年3月25日 上午8:22:06
 */
public class P2PRespBody extends BaseBody {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(P2PRespBody.class);

	/**
	 * @param args
	 *
	 * @author tanyaowu
	 */
	public static void main(String[] args) {

	}

	//消息内容，必填
	private String text;

	//一般情况还需要带上发送消息的用户昵称等信息，showcase中略过

	//消息是谁发的
	private String fromUserid;

	/**
	 *
	 * @author tanyaowu
	 */
	public P2PRespBody() {

	}

	/**
	 * @return the fromUserid
	 */
	public String getFromUserid() {
		return fromUserid;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param fromUserid the fromUserid to set
	 */
	public void setFromUserid(String fromUserid) {
		this.fromUserid = fromUserid;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
