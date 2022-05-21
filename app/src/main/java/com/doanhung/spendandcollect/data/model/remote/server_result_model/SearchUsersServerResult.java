package com.doanhung.spendandcollect.data.model.remote.server_result_model;

import com.doanhung.spendandcollect.data.model.remote.model.SearchUser;

import java.util.List;

public class SearchUsersServerResult extends ServerResult {
    private List<SearchUser> users;

    public SearchUsersServerResult(boolean success, String message, List<SearchUser> users) {
        super(success, message);
        this.users = users;
    }

    public List<SearchUser> getUsers() {
        return users;
    }

    public void setUsers(List<SearchUser> users) {
        this.users = users;
    }
}
