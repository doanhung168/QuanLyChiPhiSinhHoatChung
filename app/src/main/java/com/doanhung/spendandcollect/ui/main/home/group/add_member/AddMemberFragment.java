package com.doanhung.spendandcollect.ui.main.home.group.add_member;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.doanhung.spendandcollect.data.model.remote.model.SimpleUser;
import com.doanhung.spendandcollect.databinding.FragmentAddMemberBinding;
import com.doanhung.spendandcollect.ui.main.AuthViewModel;
import com.doanhung.spendandcollect.ui.main.HideBottomBarInterface;
import com.doanhung.spendandcollect.util.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class AddMemberFragment extends Fragment implements AddMemberAdapter.AddMemberAdapterInterface {

    private static final String TAG = "AddMemberFragment";
    private FragmentAddMemberBinding addMemberBinding;
    private AddMemberAdapter addMemberAdapter;
    private AddMemberAdapter2 addMemberAdapter2;
    private HideBottomBarInterface hideBottomBarInterface;
    private BottomSheetBehavior<ConstraintLayout> btSheet;
    private NavController navController;

    private ArrayList<SimpleUser> users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addMemberBinding = FragmentAddMemberBinding.inflate(inflater, container, false);
        return addMemberBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btSheet = BottomSheetBehavior.from(addMemberBinding.bottomSheetRoot);
        btSheet.setDraggable(false);
        btSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        navController = Navigation.findNavController(view);

        users = new ArrayList<>();


        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getFriend(Util.getToken(requireContext()));

        addMemberAdapter = new AddMemberAdapter(SimpleUser.diffCallback, this);
        addMemberBinding.rcvFriend.setAdapter(addMemberAdapter);
        addMemberBinding.rcvFriend.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        authViewModel.getFriend().observe(getViewLifecycleOwner(), friend -> {
            addMemberAdapter.submitList(friend.getFriends());
        });


        addMemberAdapter2 = new AddMemberAdapter2(SimpleUser.diffCallback);
        addMemberBinding.rcvSelected.setAdapter(addMemberAdapter2);
        addMemberBinding.rcvSelected.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        addMemberBinding.toolbar.setNavigationOnClickListener(v -> {
            navController.popBackStack();
        });

        addMemberBinding.btnDone.setOnClickListener(v -> {

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
    public void onChecked(SimpleUser user, boolean checked) {
        if (checked) {
            users.add(user);
        } else {
            users.remove(user);
        }
        addMemberAdapter2.submitList(users);
        if (users.size() > 0) {
            btSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            btSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

    }
}
