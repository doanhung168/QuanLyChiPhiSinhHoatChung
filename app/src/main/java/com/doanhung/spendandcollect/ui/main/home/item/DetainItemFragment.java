package com.doanhung.spendandcollect.ui.main.home.item;

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

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentDetailItemBinding;
import com.doanhung.spendandcollect.ui.main.home.ItemViewModel;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.MessageEvent;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.GetUserByIDResultEvent;

public class DetainItemFragment extends Fragment {

    private ItemViewModel itemViewModel;
    private NavController mainNavController;
    private FragmentDetailItemBinding fragmentDetailItemBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        mainNavController = Navigation.findNavController(requireActivity(), R.id.mainNavHost);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentDetailItemBinding = FragmentDetailItemBinding.inflate(inflater, container, false);
        return fragmentDetailItemBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        itemViewModel.getUserById(
//                itemViewModel.getItem().getValue().getOwner(),
//                Util.getToken(requireContext())
//        );

        fragmentDetailItemBinding.setLifecycleOwner(getViewLifecycleOwner());
        fragmentDetailItemBinding.setItem(itemViewModel.getItem().getValue());

        itemViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {
            if (event.getName().equals(Event.MESSAGE_EVENT)) {
                Toast.makeText(requireContext(), ((MessageEvent) event).getMessage(), Toast.LENGTH_LONG).show();
            } else if (event.getName().equals(Event.GET_USER_BY_ID_EVENT)) {
                fragmentDetailItemBinding.tvOwner.setText(
                        ((GetUserByIDResultEvent) event).getGetUserResult().getUser().getUserName()
                );
            }
        });

        fragmentDetailItemBinding.toolbar.setNavigationOnClickListener(v -> {
            mainNavController.popBackStack();
        });
    }
}
