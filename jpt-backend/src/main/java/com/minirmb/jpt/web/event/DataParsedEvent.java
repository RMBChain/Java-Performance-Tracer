package com.minirmb.jpt.web.event;

import org.springframework.context.ApplicationEvent;

public class DataParsedEvent extends ApplicationEvent {
    public DataParsedEvent(long lastedTime) {
        super(lastedTime);
    }
}
