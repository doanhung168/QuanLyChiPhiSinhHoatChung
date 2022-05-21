package com.doanhung.spendandcollect.ui.main.home.group;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.spendandcollect.data.model.remote.model.Group;
import com.doanhung.spendandcollect.databinding.GroupItemBinding;

public class GroupAdapter extends ListAdapter<Group, GroupAdapter.GroupViewHolder> {

    private final GroupInterface groupInterface;

    public GroupAdapter(@NonNull DiffUtil.ItemCallback<Group> diffCallback, GroupInterface groupInterface) {
        super(diffCallback);
        this.groupInterface = groupInterface;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupItemBinding groupItemBinding =
                GroupItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        groupItemBinding.setGroupInterface(groupInterface);
        return new GroupViewHolder(groupItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = getItem(position);
        holder.groupItemBinding.setGroup(group);
        holder.groupItemBinding.executePendingBindings();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        private final GroupItemBinding groupItemBinding;

        public GroupViewHolder(@NonNull GroupItemBinding groupItemBinding) {
            super(groupItemBinding.getRoot());
            this.groupItemBinding = groupItemBinding;
        }
    }

    public interface GroupInterface {
        void onClickItem(Group group);
    }

}
