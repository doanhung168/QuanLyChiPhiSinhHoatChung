package com.doanhung.spendandcollect.util.event;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetUserResult;

public class GetUserByIDResultEvent extends Event{
    private GetUserResult getUserResult;

    public GetUserByIDResultEvent(String name, GetUserResult getUserResult) {
        super(name);
        this.getUserResult = getUserResult;
    }

    public GetUserResult getGetUserResult() {
        return getUserResult;
    }

    public void setGetUserResult(GetUserResult getUserResult) {
        this.getUserResult = getUserResult;
    }
}
