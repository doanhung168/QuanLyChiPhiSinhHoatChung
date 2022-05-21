package com.doanhung.spendandcollect.ui.main.home.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.spendandcollect.data.model.remote.model.SearchUser;
import com.doanhung.spendandcollect.databinding.UserItemBinding;

public class SearchUserAdapter extends ListAdapter<SearchUser, SearchUserAdapter.SearchUserViewHolder> {

    private static final String TAG = "SearchUserAdapter";
    private final UserInterface userInterface;
    private SearchUser auth;

    public SearchUserAdapter(@NonNull DiffUtil.ItemCallback<SearchUser> diffCallback, UserInterface userInterface) {
        super(diffCallback);
        this.userInterface = userInterface;
    }

    public void setAuth(SearchUser auth) {
        this.auth = auth;
    }

    @NonNull
    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding userItemBinding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        userItemBinding.setUserInterface(userInterface);
        return new SearchUserViewHolder(userItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder holder, int position) {
        SearchUser searchUser = getItem(position);
        holder.userItemBinding.setSearchUser(searchUser);
        holder.userItemBinding.setAuth(auth);
        holder.userItemBinding.executePendingBindings();
    }

    static class SearchUserViewHolder extends RecyclerView.ViewHolder {

        private final UserItemBinding userItemBinding;

        public SearchUserViewHolder(@NonNull UserItemBinding userItemBinding) {
            super(userItemBinding.getRoot());
            this.userItemBinding = userItemBinding;
        }
    }

    public interface UserInterface {
        void onClickFriendBtn(SearchUser searchUser, SearchUser auth);
    }
}
