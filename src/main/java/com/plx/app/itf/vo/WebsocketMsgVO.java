package com.plx.app.itf.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class WebsocketMsgVO implements Serializable {
	private String fromUser;
	private List<String> toUsers;
	private String Type;
	private String msg;
}
