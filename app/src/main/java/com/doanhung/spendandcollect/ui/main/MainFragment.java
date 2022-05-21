package com.doanhung.spendandcollect.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentMainBinding;


public class MainFragment extends Fragment implements HideBottomBarInterface {

    private static final String TAG = "MainFragment";
    private FragmentMainBinding fragmentMainBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fragmentManager = getChildFragmentManager();
        NavHostFragment mainNavHostFragment =
                (NavHostFragment) fragmentManager.findFragmentById(R.id.mainNavHost);
        NavController mainNavController = mainNavHostFragment.getNavController();
        NavigationUI.setupWithNavController(fragmentMainBinding.btNav, mainNavController);
    }

    @Override
    public void onDestroyView() {
        fragmentMainBinding = null;
        super.onDestroyView();
    }

    @Override
    public void hide(boolean hide) {
        if (hide) {
            fragmentMainBinding.btNav.setVisibility(View.GONE);
        } else {
            fragmentMainBinding.btNav.setVisibility(View.VISIBLE);
        }
    }
}