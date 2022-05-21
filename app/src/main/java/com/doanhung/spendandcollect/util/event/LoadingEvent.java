package com.doanhung.spendandcollect.util.event;

public class LoadingEvent extends Event {
    private boolean loading;

    public LoadingEvent(String name, boolean loading) {
        super(name);
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
