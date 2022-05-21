package com.doanhung.spendandcollect.data.model.remote.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Group {

    @SerializedName("_id")
    private String id;
    private String groupName;
    private String roomMaster;
    private String groupAvatar;
    private List<String> members;
    private String createdAt;
    private String updatedAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRoomMaster() {
        return roomMaster;
    }

    public void setRoomMaster(String roomMaster) {
        this.roomMaster = roomMaster;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
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


    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String avatar) {
        this.groupAvatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(getGroupName(), group.getGroupName()) && Objects.equals(getGroupAvatar(), group.getGroupAvatar()) && Objects.equals(getMembers(), group.getMembers());
    }


    public static final DiffUtil.ItemCallback<Group> diffCallBack =
            new DiffUtil.ItemCallback<Group>() {
                @Override
                public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
