package com.doanhung.spendandcollect.ui.main.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.doanhung.spendandcollect.data.model.remote.model.SimpleUser;
import com.doanhung.spendandcollect.databinding.FragmentAccessFriendBinding;
import com.doanhung.spendandcollect.ui.main.AuthViewModel;
import com.doanhung.spendandcollect.ui.main.HideBottomBarInterface;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.MessageEvent;

public class AcceptFriendFragment extends Fragment implements AcceptFriendAdapter.AcceptFriendInterface {

    private static final String TAG = "AcceptFriendFragment";
    private FragmentAccessFriendBinding binding;
    private HideBottomBarInterface hideBottomBarInterface;
    private AuthViewModel authViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccessFriendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        binding.toolbar.setNavigationOnClickListener(v -> {
            navController.popBackStack();
        });

        authViewModel = new ViewModelProvider(getParentFragment()).get(AuthViewModel.class);
        authViewModel.getFriend(Util.getToken(requireContext()));

        AcceptFriendAdapter acceptFriendAdapter =
                new AcceptFriendAdapter(SimpleUser.diffCallback, this);
        binding.rcvAccessFriend.setAdapter(acceptFriendAdapter);
        binding.rcvAccessFriend.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );
        authViewModel.getFriend().observe(getViewLifecycleOwner(), friend ->
                acceptFriendAdapter.submitList(friend.getFriendRequests())
        );

        authViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {
            switch (event.getName()) {
                case Event.MESSAGE_EVENT:
                    Toast.makeText(requireContext(), ((MessageEvent) event).getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case Event.REFRESH_DATA_EVENT:
                    authViewModel.getFriend(Util.getToken(requireContext()));
                    break;
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
    public void accept(String userId) {
        authViewModel.addFriend(userId, Util.getToken(requireContext()));
    }

    @Override
    public void noAccept(String userId) {
        authViewModel.removeAddingFriend(userId, Util.getToken(requireContext()));
    }
}