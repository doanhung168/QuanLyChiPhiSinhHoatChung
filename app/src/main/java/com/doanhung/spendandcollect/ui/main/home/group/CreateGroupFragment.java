package com.doanhung.spendandcollect.ui.main.home.group;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentAddGroupItemBinding;
import com.doanhung.spendandcollect.ui.main.HideBottomBarInterface;
import com.doanhung.spendandcollect.ui.main.home.ItemViewModel;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.CreateGroupResultEvent;
import com.doanhung.spendandcollect.util.event.MessageEvent;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.LoadingEvent;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateGroupFragment extends Fragment {

    public static final String TAG = "AddItemFragment";
    private FragmentAddGroupItemBinding binding;
    private NavController navController;
    private HideBottomBarInterface hideBottomBarInterface;

    private ItemViewModel itemViewModel;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            startActivityForGettingImage();
                        } else {
                            Toast.makeText(requireContext(), "Permission deny!", Toast.LENGTH_LONG).show();
                        }
                    }
            );

    private final ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {


                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        itemViewModel.setAvatarGroupUri(uri);
                        binding.imvGroupAvatar.setImageURI(uri);
                    } else {
                        binding.imvGroupAvatar.setImageResource(R.drawable.ic_image);
                        itemViewModel.setAvatarGroupUri(null);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddGroupItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        setUpToolBar();

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        binding.imvGroupAvatar.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivityForGettingImage();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

            } else {
                startActivityForGettingImage();
            }

        });

        binding.btnOK.setOnClickListener(v -> {
            String groupName = binding.edtGroupName.getText().toString().trim();
            if (TextUtils.isEmpty(groupName)) {
                Toast.makeText(getContext(), "Require Group Name", Toast.LENGTH_LONG).show();
                return;
            }

            MultipartBody.Part groupAvatarPart = null;
            if (itemViewModel.getAvatarGroupUri() != null) {
                File file = new File(Util.getPathFromUri(requireContext(), itemViewModel.getAvatarGroupUri()));

                RequestBody groupAvatarRequestBody = RequestBody.create(
                        file,
                        MediaType.parse("image/*")
                );

                groupAvatarPart = MultipartBody.Part.createFormData(
                        "groupAvatar",
                        file.getName(),
                        groupAvatarRequestBody);
            }

            RequestBody groupNamePart = RequestBody.create(groupName, MediaType.parse("text/plain"));
            itemViewModel.createGroup(groupAvatarPart, groupNamePart, Util.getToken(requireContext()));
        });

        itemViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {
            switch (event.getName()) {
                case Event.MESSAGE_EVENT:
                    Toast.makeText(requireContext(), ((MessageEvent) event).getMessage(), Toast.LENGTH_LONG).show();
                    break;
                case Event.CREATE_GROUP_RESULT_EVENT:
                    if (((CreateGroupResultEvent) event).getCreateGroupResult().isSuccess()) {
                        Toast.makeText(
                                requireContext(),
                                ((CreateGroupResultEvent) event).getCreateGroupResult().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        navController.popBackStack();
                    }
                    break;
                case Event.LOADING_EVENT:
                    if (((LoadingEvent) event).isLoading()) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    } else {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
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
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private void setUpToolBar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(v -> {
            Util.hideKeyboard(requireActivity());
            navController.popBackStack();
        });
    }

    private void startActivityForGettingImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        getImage.launch(intent);
    }
}
