package com.doanhung.spendandcollect.ui.main.home.search;

import com.doanhung.spendandcollect.util.event.Event;

public class GetAuthDoneEvent extends Event {
    private boolean isSuccess;

    public GetAuthDoneEvent(String name, boolean isSuccess) {
        super(name);
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
