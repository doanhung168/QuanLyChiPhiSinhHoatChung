package com.doanhung.spendandcollect.ui.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.data.model.remote.model.Group;
import com.doanhung.spendandcollect.databinding.FragmentHomeBinding;
import com.doanhung.spendandcollect.ui.main.home.group.GroupAdapter;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.CreateGroupResultEvent;
import com.doanhung.spendandcollect.util.event.Event;

public class HomeFragment extends Fragment implements GroupAdapter.GroupInterface {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding fragmentHomeBinding;
    private ItemViewModel itemViewModel;
    private NavController navController;

    private GroupAdapter groupAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolBar();

        navController = Navigation.findNavController(view);
        groupAdapter = new GroupAdapter(Group.diffCallBack, this);
        fragmentHomeBinding.rcvGroup.setAdapter(groupAdapter);

        itemViewModel.getGroup(Util.getToken(requireContext()));

        itemViewModel.getGroupOfUser().observe(getViewLifecycleOwner(), group -> {
            if (group.size() == 0) {
                fragmentHomeBinding.rcvGroup.setVisibility(View.INVISIBLE);
                fragmentHomeBinding.layoutSupport.setVisibility(View.VISIBLE);
            } else {
                fragmentHomeBinding.rcvGroup.setVisibility(View.VISIBLE);
                fragmentHomeBinding.layoutSupport.setVisibility(View.INVISIBLE);
                groupAdapter.submitList(group);
            }
        });

    }

    private void setUpToolBar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(fragmentHomeBinding.toolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.mainNavHost);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    @Override
    public void onClickItem(Group group) {
        itemViewModel.setCurrentGroup(group);
        navController.navigate(R.id.action_homeFragment_to_groupFragment);
    }
}