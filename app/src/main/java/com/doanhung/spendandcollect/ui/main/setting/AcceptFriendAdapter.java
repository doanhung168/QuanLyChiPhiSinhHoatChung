package com.doanhung.spendandcollect.ui.main.setting;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.spendandcollect.data.model.remote.model.Friend;
import com.doanhung.spendandcollect.data.model.remote.model.SimpleUser;
import com.doanhung.spendandcollect.databinding.ItemAcceptFriendBinding;

public class AcceptFriendAdapter extends ListAdapter<SimpleUser, AcceptFriendAdapter.AccessFriendViewHolder> {

    private final AcceptFriendInterface acceptFriendInterface;

    protected AcceptFriendAdapter(@NonNull DiffUtil.ItemCallback<SimpleUser> diffCallback, AcceptFriendInterface acceptFriendInterface) {
        super(diffCallback);
        this.acceptFriendInterface = acceptFriendInterface;
    }

    @NonNull
    @Override
    public AccessFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAcceptFriendBinding binding =
                ItemAcceptFriendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AccessFriendViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AccessFriendViewHolder holder, int position) {
        SimpleUser simpleUser = getItem(position);
        holder.binding.setSimpleUser(simpleUser);
        holder.binding.setAcceptInterface(acceptFriendInterface);
        holder.binding.executePendingBindings();
    }

    static class AccessFriendViewHolder extends RecyclerView.ViewHolder {
        private final ItemAcceptFriendBinding binding;
        public AccessFriendViewHolder(@NonNull ItemAcceptFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface AcceptFriendInterface {
        void accept(String userId);
        void noAccept(String userId);
    }
}
