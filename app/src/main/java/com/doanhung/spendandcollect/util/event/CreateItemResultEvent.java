package com.doanhung.spendandcollect.util.event;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;

public class CreateItemResultEvent extends Event{
    private ServerResult createItemResult;

    public CreateItemResultEvent(String name, ServerResult createItemResult) {
        super(name);
        this.createItemResult = createItemResult;
    }

    public ServerResult getCreateItemResult() {
        return createItemResult;
    }

    public void setCreateItemResult(ServerResult createItemResult) {
        this.createItemResult = createItemResult;
    }
}
