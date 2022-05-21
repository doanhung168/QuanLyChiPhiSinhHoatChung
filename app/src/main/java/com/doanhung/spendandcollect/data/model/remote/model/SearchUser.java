package com.doanhung.spendandcollect.data.model.remote.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

public class SearchUser {

    private String id;
    private String userName;
    private String avatar;
    private List<String> friends;
    private List<String> friendRequests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchUser that = (SearchUser) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getAvatar(), that.getAvatar()) && Objects.equals(getFriends(), that.getFriends()) && Objects.equals(getFriendRequests(), that.getFriendRequests());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getAvatar(), getFriends(), getFriendRequests());
    }

    public static DiffUtil.ItemCallback<SearchUser> diffCallback = new DiffUtil.ItemCallback<SearchUser>() {
        @Override
        public boolean areItemsTheSame(@NonNull SearchUser oldItem, @NonNull SearchUser newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SearchUser oldItem, @NonNull SearchUser newItem) {
            return oldItem.equals(newItem);
        }
    };
}
