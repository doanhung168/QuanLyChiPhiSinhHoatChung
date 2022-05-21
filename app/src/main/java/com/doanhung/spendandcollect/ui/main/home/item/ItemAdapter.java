package com.doanhung.spendandcollect.ui.main.home.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.spendandcollect.data.model.remote.model.Item;
import com.doanhung.spendandcollect.databinding.ItemItemBinding;

public class ItemAdapter extends ListAdapter<Item, ItemAdapter.ItemViewHolder> {

    private final ItemInterface itemInterface;

    public ItemAdapter(@NonNull DiffUtil.ItemCallback<Item> diffCallback, ItemInterface itemInterface) {
        super(diffCallback);
        this.itemInterface = itemInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemBinding itemItemBinding =
                ItemItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        itemItemBinding.setItemInterface(itemInterface);
        return new ItemViewHolder(itemItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = getItem(position);
        holder.itemItemBinding.setItem(item);
        holder.itemItemBinding.executePendingBindings();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemItemBinding itemItemBinding;

        public ItemViewHolder(@NonNull ItemItemBinding itemItemBinding) {
            super(itemItemBinding.getRoot());
            this.itemItemBinding = itemItemBinding;
        }
    }

    public interface ItemInterface {
        void onClickItem(Item item);
    }

}
