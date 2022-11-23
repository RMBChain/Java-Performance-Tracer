package com.minirmb.jps.web.rabbit;

import java.util.Map;
import java.util.Set;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.minirmb.jps.web.websocket.SnapshotRowWebSocketOperation;

@Component
@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {
 
	@Autowired
	private SnapshotRowWebSocketOperation snapshotRowWebSocketOperation;
	
    @RabbitHandler
    public void processp(Map<String, Object> msg) {
    	@SuppressWarnings("unchecked")
		Set<String> snapshotIdSet = (Set<String>)msg.get("snapshotIdSet");
    	if(null != snapshotIdSet) {
	    	for(String value : snapshotIdSet) {
	    		snapshotRowWebSocketOperation.broastMessage(value, "message");
	    	}
    	}
    }
}
