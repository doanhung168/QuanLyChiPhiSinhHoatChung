package com.doanhung.spendandcollect.data.model.remote.model;

public class Auth {
    private String authId;
    private String userName;
    private String avatar;
    private String friend;
    private int friendRequestNumber;
    private String authType;

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public int getFriendRequestNumber() {
        return friendRequestNumber;
    }

    public void setFriendRequestNumber(int friendRequestNumber) {
        this.friendRequestNumber = friendRequestNumber;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
