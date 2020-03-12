package com.minirmb.jds.web.controller;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.minirmb.jds.web.websocket.EndpointConfig;
import com.minirmb.jds.web.websocket.SnapshotRowWebSocketOperation;

@Scope("prototype")
@ServerEndpoint(value = "/websocket/{pageCode}", configurator = EndpointConfig.class)
@Controller
public class WebSocketController {

	@Autowired
	private SnapshotRowWebSocketOperation snapshotRowWebSocketOperation;

	@OnOpen
	public void onOpen(@PathParam("pageCode") String pageCode, Session session) {
		snapshotRowWebSocketOperation.onOpen(pageCode, session);
	}

	@OnClose
	public void onClose(@PathParam("pageCode") String pageCode, Session session) {
		snapshotRowWebSocketOperation.onClose(pageCode, session);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		snapshotRowWebSocketOperation.onMessage(message, session);
	}

	@OnError
	public void onError(Session session, Throwable error) {
		snapshotRowWebSocketOperation.onError(session, error);
	}
}
