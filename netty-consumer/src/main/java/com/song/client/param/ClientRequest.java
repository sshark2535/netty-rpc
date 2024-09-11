package com.song.client.param;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {
    private final long id;
    private Object context;

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private final AtomicLong aid = new AtomicLong(1);
    public ClientRequest() {
        id = aid.incrementAndGet();
    }

    public long getId() {
        return id;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }
}
