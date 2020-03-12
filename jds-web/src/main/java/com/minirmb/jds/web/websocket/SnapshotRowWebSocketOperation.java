package com.minirmb.jds.web.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.websocket.Session;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnapshotRowWebSocketOperation {

	/**
	 * key:pageCode
	 */
	private static Map<String, List<Session>> electricSocketMap = new ConcurrentHashMap<>();

	@Resource
	private RedisTemplate<String, Boolean> redisTemplate;

	public void setBroadcast(String sessionId, String snapshotId, Boolean isBroadcast) {
		redisTemplate.opsForValue().set("isBroadcast:" + sessionId + "-" + snapshotId, isBroadcast, 60,
				TimeUnit.SECONDS);
	}

	public Boolean isBroadcast(String sessionId, String snapshotId) {
		Boolean result = false;
		Object obj = redisTemplate.opsForValue().get("isBroadcast:" + sessionId + "-" + snapshotId);
		if (null != obj) {
			result = (Boolean) obj;
		}
		return result;
	}

	public void broastMessage(String snapshotId, String message) {
		for (List<Session> sessionList : electricSocketMap.values()) {
			for (Session session : sessionList) {
				String sessionId = session.getId();
				if (!isBroadcast(sessionId, snapshotId)) {
					try {
						session.getBasicRemote().sendText("SnapshotRowListUpdated");
						setBroadcast(sessionId, snapshotId, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		log.debug("websocket broastMessage:" + message);
	}

	public void onOpen(String pageCode, Session session) {
		List<Session> sessions = electricSocketMap.get(pageCode);
		if (null == sessions) {
			List<Session> sessionList = new ArrayList<>();
			sessionList.add(session);
			electricSocketMap.put(pageCode, sessionList);
		} else {
			sessions.add(session);
		}
		log.debug("websocket opened. session id:" + session.getId());
	}

	public void onClose(String pageCode, Session session) {
		if (electricSocketMap.containsKey(pageCode)) {
			electricSocketMap.get(pageCode).remove(session);
		}
		log.debug("websocket close. session id:" + session.getId());
	}

	public void onMessage(String message, Session session) {
		log.debug("websocket received message:" + message);
	}

	public void onError(Session session, Throwable error) {
		log.error("error on websocket!");
	}
}
