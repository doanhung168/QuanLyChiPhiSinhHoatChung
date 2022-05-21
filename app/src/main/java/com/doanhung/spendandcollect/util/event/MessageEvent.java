package com.doanhung.spendandcollect.util.event;

public class MessageEvent extends Event{
    private String message;

    public MessageEvent(String name, String message) {
        super(name);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
