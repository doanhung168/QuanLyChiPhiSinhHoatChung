package com.doanhung.spendandcollect.ui.main.home.group;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.data.model.remote.model.Item;
import com.doanhung.spendandcollect.databinding.FragmentGroupBinding;
import com.doanhung.spendandcollect.ui.main.HideBottomBarInterface;
import com.doanhung.spendandcollect.ui.main.home.ItemViewModel;
import com.doanhung.spendandcollect.ui.main.home.item.ItemAdapter;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.MessageEvent;
import com.doanhung.spendandcollect.util.event.Event;


public class GroupFragment extends Fragment implements ItemAdapter.ItemInterface {
    private static final String TAG = "GroupFragment";
    private FragmentGroupBinding fragmentGroupBinding;

    private NavController navController;
    private HideBottomBarInterface hideBottomBarInterface;
    private ItemViewModel itemViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentGroupBinding = FragmentGroupBinding.inflate(inflater, container, false);
        return fragmentGroupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolBar();
        navController = Navigation.findNavController(requireActivity(), R.id.mainNavHost);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        itemViewModel.getItemOfGroup(
                itemViewModel.getCurrentGroup().getValue().getId(),
                Util.getToken(requireContext())
        );

        itemViewModel.getCurrentGroup().observe(getViewLifecycleOwner(), group -> {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(group.getGroupName());
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setSubtitle(group.getMembers().size() + " Members");
        });

        fragmentGroupBinding.btnAddItem.setOnClickListener(v -> {
            navController.navigate(R.id.action_groupFragment_to_addItemFragment);
        });


        ItemAdapter itemAdapter = new ItemAdapter(Item.diffCallback, this);
        fragmentGroupBinding.rcvItem.setAdapter(itemAdapter);
        itemViewModel.getItems().observe(getViewLifecycleOwner(), itemAdapter::submitList);

        itemViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {
            if (event.getName().equals(Event.MESSAGE_EVENT)) {
                Toast.makeText(requireContext(), ((MessageEvent) event).getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpToolBar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(fragmentGroupBinding.toolbar);

        fragmentGroupBinding.toolbar.setNavigationOnClickListener(v -> {
            navController.popBackStack();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.group_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.AddMemberFragment) {
            navController.navigate(R.id.action_groupFragment_to_addMemberFragment);
        } else if (item.getItemId() == R.id.GroupManagerFragment) {
            Log.e(TAG, "onOptionsItemSelected: group manager");
        }
        return super.onOptionsItemSelected(item);
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
    public void onDestroyView() {
        fragmentGroupBinding = null;
        super.onDestroyView();
    }

    @Override
    public void onClickItem(Item item) {
        itemViewModel.setItem(item);
        navController.navigate(R.id.action_groupFragment_to_detainItemFragment);
    }
}