package com.plx.app.itf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.plx.app.cmn.vo.LoginInfoVO;
import com.plx.app.itf.vo.WebsocketMsgVO;

/**
 * @Project KNIS
 * @Class WebsocketHandler
 * @since 2020. 7. 24.
 * @author
 * @Description :
 */
public class WebsocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(WebsocketHandler.class);

    private static final List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
    private static final Map<String, List<WebSocketSession>> userSessionMap = new HashMap<String, List<WebSocketSession>>();

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {

        logger.info("{} 연결됨", session.getId());
        final Map<String, Object> sessionAttributes = session.getAttributes();
        final LoginInfoVO loginInfo = (LoginInfoVO) sessionAttributes.get(LoginInfoVO.LOGIN_INFO);

        if(loginInfo == null) { //로그인 정보가 없을경우.
            System.out.println("로그인 정보 없음");
            return;
        }
        sessionList.add(session);
        final String userId = loginInfo.getUsrId();
        List<WebSocketSession> webSocketSessionList = userSessionMap.get(userId);

        if (webSocketSessionList == null) {
            webSocketSessionList = new ArrayList<WebSocketSession>();
            userSessionMap.put(userId, webSocketSessionList);
        }
        webSocketSessionList.add(session);
        System.out.println("로그인 아이디 : " + userId);
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        final LoginInfoVO loginInfo = (LoginInfoVO) session.getAttributes().get(LoginInfoVO.LOGIN_INFO);

        final String msg = message.getPayload();
        logger.info("{}로 부터 {} 받음", loginInfo.getUsrId(), msg);
        sendMeassage(message);

    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        sessionList.remove(session);
        System.out.println("Websocket Closed");
        final Map<String, Object> sessionAttributes = session.getAttributes();
        final LoginInfoVO loginInfo = (LoginInfoVO) sessionAttributes.get("LOGIN_INFO");
        if(loginInfo == null) { //로그인 정보가 없을경우.
            System.out.println("로그인 정보 없음");
            return;
		}
        final String userId = loginInfo.getUsrId();
		final List<WebSocketSession> webSocketSessionList = userSessionMap.get(userId);
		webSocketSessionList.remove(session);
        if(webSocketSessionList.isEmpty()){
            userSessionMap.remove(userId);
        }
        logger.info("{} 연결 끊김", userId);
    }

    public void sendMeassage(final TextMessage message) throws IOException {
        try {
            final String msg = message.getPayload();
			final WebsocketMsgVO webSocketMsgVO = new Gson().fromJson(msg, WebsocketMsgVO.class);

            for(final String userId : webSocketMsgVO.getToUsers()){
                final List<WebSocketSession> sessions = userSessionMap.get(userId);
                for(final WebSocketSession sess : sessions){
                    sess.sendMessage(message);
				}
            }
        }
        catch (final JsonParseException e) {
            sendMeassage(message);
            for (final WebSocketSession sess : sessionList) {
                sess.sendMessage(new TextMessage(message.getPayload()));
            }
		}
    }

    public void sendMeassage(WebsocketMsgVO webSocketMsgVO) throws IOException {

        TextMessage msg = new TextMessage(webSocketMsgVO.getMsg());

        List<String> users = webSocketMsgVO.getToUsers();

        if(!users.isEmpty()){
            for (final String userId : users) {
                final List<WebSocketSession> sessions = userSessionMap.get(userId);
                for (final WebSocketSession sess : sessions) {
                    sess.sendMessage(msg);
                }
            }
        } else {
            for (final WebSocketSession sess : sessionList) {
                sess.sendMessage(msg);
            }
        }
    }

    public Set<String> getLoginIdSet () {
        return userSessionMap.keySet();
	}
}