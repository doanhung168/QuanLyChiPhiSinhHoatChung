package com.doanhung.spendandcollect.data.model.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Friend {

    @SerializedName("_id")
    private String id;
    private List<SimpleUser> friends;
    private List<SimpleUser> friendRequests;

    public Friend() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SimpleUser> getFriends() {
        return friends;
    }

    public void setFriends(List<SimpleUser> friends) {
        this.friends = friends;
    }

    public List<SimpleUser> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<SimpleUser> friendRequests) {
        this.friendRequests = friendRequests;
    }
}
