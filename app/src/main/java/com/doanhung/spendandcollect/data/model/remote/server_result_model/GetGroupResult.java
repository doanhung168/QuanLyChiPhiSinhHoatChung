package com.doanhung.spendandcollect.data.model.remote.server_result_model;

import com.doanhung.spendandcollect.data.model.remote.model.Group;

import java.util.List;

public class GetGroupResult extends ServerResult {

    private List<Group> groups;

    public GetGroupResult(boolean success, String message, List<Group> groups) {
        super(success, message);
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
