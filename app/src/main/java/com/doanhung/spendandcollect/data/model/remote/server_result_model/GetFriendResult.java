package com.doanhung.spendandcollect.data.model.remote.server_result_model;

import com.doanhung.spendandcollect.data.model.remote.model.Friend;

public class GetFriendResult extends ServerResult {
    private Friend friend;

    public GetFriendResult(boolean success, String message, Friend friend) {
        super(success, message);
        this.friend = friend;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }
}
