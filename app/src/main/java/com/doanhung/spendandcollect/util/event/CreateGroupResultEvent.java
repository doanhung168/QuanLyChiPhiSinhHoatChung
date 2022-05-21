package com.doanhung.spendandcollect.util.event;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;

public class CreateGroupResultEvent extends Event{
    private ServerResult createGroupResult;
    public CreateGroupResultEvent(String name, ServerResult createGroupResult) {
        super(name);
        this.createGroupResult = createGroupResult;
    }

    public ServerResult getCreateGroupResult() {
        return createGroupResult;
    }

    public void setCreateGroupResult(ServerResult createGroupResult) {
        this.createGroupResult = createGroupResult;
    }
}
