package com.doanhung.spendandcollect.data.model.remote.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class SimpleUser {

    @SerializedName("_id")
    private String id;
    private String userName;
    @SerializedName("image")
    private String avatar;
    private String createdAt;
    private String updatedAt;
    private String friend;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleUser that = (SimpleUser) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getAvatar(), that.getAvatar()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getUpdatedAt(), that.getUpdatedAt()) && Objects.equals(getFriend(), that.getFriend());
    }

    public static final DiffUtil.ItemCallback<SimpleUser> diffCallback = new DiffUtil.ItemCallback<SimpleUser>() {
        @Override
        public boolean areItemsTheSame(@NonNull SimpleUser oldItem, @NonNull SimpleUser newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SimpleUser oldItem, @NonNull SimpleUser newItem) {
            return oldItem.equals(newItem);
        }
    };
}
