package com.doanhung.spendandcollect.data.model.remote.server_result_model;

import com.doanhung.spendandcollect.data.model.remote.model.User;

public class GetUserResult extends ServerResult {

    private User user;

    public GetUserResult(boolean success, String message, User user) {
        super(success, message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
