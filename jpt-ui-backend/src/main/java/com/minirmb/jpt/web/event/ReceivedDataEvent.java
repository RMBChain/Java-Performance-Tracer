package com.minirmb.jpt.web.event;

import org.springframework.context.ApplicationEvent;

public class ReceivedDataEvent extends ApplicationEvent {
    private long lastedTime = 0;
    public long getLastedTime() {
        return lastedTime;
    }

    public ReceivedDataEvent(String data) {
        super(data);
        lastedTime = System.currentTimeMillis();
    }
}
