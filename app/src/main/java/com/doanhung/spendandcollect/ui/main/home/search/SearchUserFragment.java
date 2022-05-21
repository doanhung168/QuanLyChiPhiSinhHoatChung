package com.doanhung.spendandcollect.ui.main.home.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.data.model.remote.model.SearchUser;
import com.doanhung.spendandcollect.databinding.FragmentSearchUserBinding;
import com.doanhung.spendandcollect.ui.main.HideBottomBarInterface;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.MessageEvent;

import java.util.List;

public class SearchUserFragment extends Fragment implements SearchUserAdapter.UserInterface {

    private static final String TAG = "SearchUserFragment";
    private FragmentSearchUserBinding fragmentSearchUserBinding;
    private SearchUserViewModel searchUserViewModel;

    private HideBottomBarInterface hideBottomBarInterface;
    private NavController navController;

    private String authToken;
    private String textQuery;
    private SearchUserAdapter searchUserAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSearchUserBinding = FragmentSearchUserBinding.inflate(inflater, container, false);
        return fragmentSearchUserBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolBar();

        authToken = Util.getToken(requireContext());
        searchUserViewModel = new ViewModelProvider(this).get(SearchUserViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.mainNavHost);

        searchUserAdapter = new SearchUserAdapter(
                SearchUser.diffCallback,
                this
        );
        fragmentSearchUserBinding.rcvSearchUser.setAdapter(searchUserAdapter);

        searchUserViewModel.getSingleLiveEvent().observe(getViewLifecycleOwner(), event -> {
            if (event.getName().equals(Event.MESSAGE_EVENT)) {
                Toast.makeText(requireContext(), ((MessageEvent) event).getMessage(), Toast.LENGTH_SHORT).show();
            } else if (event.getName().equals(Event.LOADING_STATE_FRIEND)) {
                searchUserViewModel.searchUser(textQuery, authToken);
            }
        });

        searchUserViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            searchUserAdapter.setAuth(searchUserViewModel.getAuth().getValue());
            searchUserAdapter.submitList(users);
            if (users.size() == 0) {
                fragmentSearchUserBinding.tvNoResult.setVisibility(View.VISIBLE);
            } else {
                fragmentSearchUserBinding.tvNoResult.setVisibility(View.GONE);
            }
        });

    }

    private void setUpToolBar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(fragmentSearchUserBinding.toolbar);
        fragmentSearchUserBinding.toolbar.setNavigationOnClickListener(v -> {
            navController.popBackStack();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.searchMenuItem);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textQuery = newText;
                if (textQuery.equals("")) {
                    fragmentSearchUserBinding.search.setVisibility(View.GONE);
                    fragmentSearchUserBinding.searchHistory.setVisibility(View.VISIBLE);
                } else {
                    fragmentSearchUserBinding.searchHistory.setVisibility(View.GONE);
                    fragmentSearchUserBinding.search.setVisibility(View.VISIBLE);
                    searchUserViewModel.searchUser(textQuery, authToken);
                }
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        hideBottomBarInterface = (HideBottomBarInterface) getParentFragment().getParentFragment();
        hideBottomBarInterface.hide(true);
        super.onStart();
    }

    @Override
    public void onStop() {
        hideBottomBarInterface.hide(false);
        super.onStop();
    }

    @Override
    public void onClickFriendBtn(SearchUser searchUser, SearchUser auth) {
        FriendState friendState = getFriendState(searchUser, auth);
        switch (friendState) {
            case FRIEND_REQUEST: {
                Log.e(TAG, "onClickFriendBtn: remove friend request");
                searchUserViewModel.removeFriendRequest(searchUser.getId(), authToken);
                break;
            }
            case FRIEND: {
                Log.e(TAG, "onClickFriendBtn: remove friend");
                searchUserViewModel.disConnectFriend(searchUser.getId(), authToken);
                break;
            }
            case FRIEND_REQUEST2: {
                searchUserViewModel.addFriend(searchUser.getId(), authToken);
                Log.e(TAG, "onClickFriendBtn: add friend");
                break;
            }
            case NON_FRIEND: {
                Log.e(TAG, "onClickFriendBtn: add request friend");
                searchUserViewModel.addFriendRequest(searchUser.getId(), authToken);
                break;
            }
        }


    }

    private FriendState getFriendState(SearchUser searchUser, SearchUser auth) {
        List<String> authFriends = auth.getFriendRequests();
        for (String userId : authFriends) {
            if (userId.equals(searchUser.getId())) {
                return FriendState.FRIEND_REQUEST2;
            }
        }

        List<String> friendRequests = searchUser.getFriendRequests();
        for (String userId : friendRequests) {
            if (userId.equals(auth.getId())) {
                return FriendState.FRIEND_REQUEST;
            }
        }

        List<String> friends = searchUser.getFriends();
        for (String userId : friends) {
            if (userId.equals(auth.getId())) {
                return FriendState.FRIEND;
            }
        }

        return FriendState.NON_FRIEND;
    }

    enum FriendState {
        FRIEND, FRIEND_REQUEST, NON_FRIEND, FRIEND_REQUEST2
    }
}
