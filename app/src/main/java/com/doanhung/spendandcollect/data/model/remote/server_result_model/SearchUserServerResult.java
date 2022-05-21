package com.doanhung.spendandcollect.data.model.remote.server_result_model;

import com.doanhung.spendandcollect.data.model.remote.model.SearchUser;

import java.util.List;

public class SearchUserServerResult extends ServerResult {
    private SearchUser auth;

    public SearchUserServerResult(boolean success, String message, SearchUser auth) {
        super(success, message);
        this.auth = auth;
    }

    public SearchUser getAuth() {
        return auth;
    }

    public void setAuth(SearchUser auth) {
        this.auth = auth;
    }
}
