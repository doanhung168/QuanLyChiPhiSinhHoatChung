package com.doanhung.spendandcollect.ui.main.home.group.add_member;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.spendandcollect.data.model.remote.model.SimpleUser;
import com.doanhung.spendandcollect.databinding.AddMemberItemBinding;

public class AddMemberAdapter extends ListAdapter<SimpleUser, AddMemberAdapter.AddMemberViewHolder> {

    private final AddMemberAdapterInterface adapterInterface;

    protected AddMemberAdapter(
            @NonNull DiffUtil.ItemCallback<SimpleUser> diffCallback,
            AddMemberAdapterInterface adapterInterface
    ) {
        super(diffCallback);
        this.adapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public AddMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddMemberItemBinding binding =
                AddMemberItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AddMemberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMemberViewHolder holder, int position) {
        SimpleUser simpleUser = getItem(position);
        holder.binding.setUser(simpleUser);
        holder.binding.setAdapterInterface(adapterInterface);
        holder.binding.executePendingBindings();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class AddMemberViewHolder extends RecyclerView.ViewHolder {

        private final AddMemberItemBinding binding;

        public AddMemberViewHolder(@NonNull AddMemberItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface AddMemberAdapterInterface {
        void onChecked(SimpleUser user, boolean checked);
    }
}
