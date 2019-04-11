package org.tio.examples.showcase.common.packets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送消息响应
 * @author tanyaowu
 * 2017年3月25日 上午8:22:06
 */
public class GroupMsgRespBody extends BaseBody {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(GroupMsgRespBody.class);

	/**
	 * @param args
	 *
	 * @author tanyaowu
	 */
	public static void main(String[] args) {

	}

	//消息内容，必填
	private String text;

	//消息是谁发的
	private String fromUserid;

	//一般情况还需要带上发送消息的用户昵称等信息，showcase中略过

	//发消息到哪个组，可以为空
	private String toGroup;

	/**
	 *
	 * @author tanyaowu
	 */
	public GroupMsgRespBody() {

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
	 * @return the toGroup
	 */
	public String getToGroup() {
		return toGroup;
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

	/**
	 * @param toGroup the toGroup to set
	 */
	public void setToGroup(String toGroup) {
		this.toGroup = toGroup;
	}
}
