package com.peoplehero.mauriciomartins.peoplehero.view.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peoplehero.mauriciomartins.peoplehero.ViewModel.UserProfileViewModel;

/**
 * Created by mauriciomartins on 20/07/17.
 */

public class UserProfileFragment extends LifecycleFragment {
    private static final String UID_KEY = "uid";
    private UserProfileViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String userId = getArguments().getString(UID_KEY);
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        viewModel.init(userId);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater,
//                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.user_profile, container, false);
//    }
}