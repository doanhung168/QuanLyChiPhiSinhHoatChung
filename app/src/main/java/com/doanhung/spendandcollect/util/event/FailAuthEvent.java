package com.doanhung.spendandcollect.util.event;

public class FailAuthEvent extends Event{
    private String message;

    public FailAuthEvent(String name, String message) {
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
