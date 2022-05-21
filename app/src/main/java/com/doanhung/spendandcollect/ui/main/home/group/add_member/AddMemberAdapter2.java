package com.doanhung.spendandcollect.ui.main.home.group.add_member;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.spendandcollect.data.model.remote.model.SimpleUser;
import com.doanhung.spendandcollect.databinding.AddMemberItem2Binding;

public class AddMemberAdapter2 extends ListAdapter<SimpleUser, AddMemberAdapter2.AddMemberAdapter2ViewHolder> {

    protected AddMemberAdapter2(@NonNull DiffUtil.ItemCallback<SimpleUser> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AddMemberAdapter2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddMemberItem2Binding binding = AddMemberItem2Binding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AddMemberAdapter2ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMemberAdapter2ViewHolder holder, int position) {
        SimpleUser simpleUser = getItem(position);
        holder.binding.setUser(simpleUser);
        holder.binding.executePendingBindings();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class AddMemberAdapter2ViewHolder extends RecyclerView.ViewHolder {
        private final AddMemberItem2Binding binding;

        public AddMemberAdapter2ViewHolder(@NonNull AddMemberItem2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
